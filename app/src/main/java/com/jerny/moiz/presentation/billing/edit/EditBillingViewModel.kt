package com.jerny.moiz.presentation.billing.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.SettlementsDto
import com.jerny.moiz.domain.model.InputCostEntity
import com.jerny.moiz.domain.usecase.GetBillingDetailUseCase
import com.jerny.moiz.domain.usecase.GetBillingMembersUseCase
import com.jerny.moiz.domain.usecase.PutBillingsUseCase
import com.jerny.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBillingViewModel @Inject constructor(
    private val getBillingDetailUseCase: GetBillingDetailUseCase,
    private val getBillingMembersUseCase: GetBillingMembersUseCase,
    private val putBillingsUseCase: PutBillingsUseCase
) :
    ViewModel() {

    var totalAmount: Double = 0.0
    var checkCnt: Int = 0
    var imageCnt: Int = 0

    private val _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    private val _members = MutableLiveData<List<BillingMembersDto>>()
    val members: LiveData<List<BillingMembersDto>> = _members

    private val _temp = MutableLiveData<List<InputCostEntity>?>()
    val temp: LiveData<List<InputCostEntity>?> = _temp

    private val _paramList = MutableLiveData<PostBillingDto>()
    val paramList: LiveData<PostBillingDto> = _paramList

    fun getBillingDetail(id: Int, token: String) {
        viewModelScope.launch {
            getBillingDetailUseCase.invoke(id, token).let {
                getBillingMembers(it.travel!!, token)
                totalAmount = it.total_amount?.toDouble()!!
                imageCnt = it.images?.size!!
                checkCnt = it.participants!!.size
                _paramList.value = PostBillingDto(
                    it.title,
                    it.participants.find { participant ->
                        participant.user?.nickname == it.paid_by
                    }?.user?.id,
                    it.paid_date,
                    it.category,
                    it.total_amount_currency,
                    it.participants.map { participants ->
                        SettlementsDto(
                            participants.user?.id,
                            participants.total_amount?.toDouble()
                        )
                    }
                )
            }
        }
    }

    fun getBillingMembers(id: Int, token: String) {
        viewModelScope.launch {
            getBillingMembersUseCase.invoke(id, token).let {
                _members.value = it
                val temp = ArrayList<InputCostEntity>()
                paramList.value?.settlements?.forEach { settlement ->
                    it.forEach { member ->
                        if (member.id == settlement?.user)
                            temp.add(
                                InputCostEntity(
                                    settlement?.user!!, true,
                                    member.name.toString(), settlement.amount!!
                                )
                            )
                    }
                }.apply { _temp.value = temp }
            }
        }
    }

    fun putBilling(id: Int, token: String, imgList: List<FileResult>) {
        viewModelScope.launch {
            putBillingsUseCase.invoke(id, token, paramList.value!!, imgList)
        }
    }

    private fun isValidate() = paramList.value?.let {
        _isValidated.value = it.title != "" &&
                it.category != "" &&
                it.currency != "" &&
                it.paid_date != "" &&
                it.paid_by != 0 &&
                it.settlements.isNotEmpty()
    }

    fun updateParam(type: Int, value: Any?) {
        when (type) {
            0 -> {
                paramList.value?.title = value as String?
                isValidate()
            }

            1 -> {
                paramList.value?.category = value as String?
                isValidate()
            }

            2 -> {
                paramList.value?.currency = value as String?
                isValidate()
            }

            3 -> {
                paramList.value?.paid_date = value as String?
                isValidate()
            }

            4 -> {
                paramList.value?.paid_by = value as Int?
                isValidate()
            }

            5 -> {
                paramList.value?.settlements = _temp.value?.map {
                    SettlementsDto(
                        it.userId,
                        it.cost
                    )
                }!!
                isValidate()
            }
        }
    }

    fun updateTotalAmount() {
        val temp = _temp.value?.toMutableList()
        temp?.forEach {
            if (it.isChecked) {
                it.cost = totalAmount / checkCnt
            } else {
                it.cost = 0.0
            }
        }
        _temp.value = temp

        updateParam(5, null)
    }

    fun changeCost(data: InputCostEntity) {
        val temp = _temp.value?.toMutableList()
        temp?.forEach {
            if (it.userId == data.userId) {
                totalAmount -= it.cost
                totalAmount += data.cost
                it.cost = data.cost
            }
        }
        _temp.value = temp

        updateParam(5, null)
    }

    fun updateCost(data: InputCostEntity, isDutch: Boolean) {
        if (data.isChecked) checkCnt++
        else checkCnt--

        val temp = _temp.value?.toMutableList()
        temp?.forEach {
            if (it.userId == data.userId) {
                it.isChecked = data.isChecked
            }
        }
        _temp.value = temp

        if (!isDutch) updateTotalAmount()
    }

    fun clearCost() {
        totalAmount = 0.0
        checkCnt = 0
        val temp = _temp.value?.toMutableList()
        temp?.forEach {
            it.isChecked = false
            it.cost = 0.0
        }
        _temp.value = temp

        updateParam(5, null)
    }
}