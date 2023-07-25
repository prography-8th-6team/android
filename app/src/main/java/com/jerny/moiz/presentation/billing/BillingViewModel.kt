package com.jerny.moiz.presentation.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.SettlementsDto
import com.jerny.moiz.domain.model.InputCostEntity
import com.jerny.moiz.domain.usecase.GetBillingMembersUseCase
import com.jerny.moiz.domain.usecase.PostBillingsUseCase
import com.jerny.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getBillingMembersUseCase: GetBillingMembersUseCase,
    private val postBillingsUseCase: PostBillingsUseCase
) :
    ViewModel() {

    private val _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    private val _members = MutableLiveData<List<BillingMembersDto>>()
    val members: LiveData<List<BillingMembersDto>> = _members

    private val _temp = MutableLiveData<List<InputCostEntity>?>()
    val temp: LiveData<List<InputCostEntity>?> = _temp

    var paramList: MutableLiveData<PostBillingDto> = PostBillingDto(
        title = "",
        category = "",
        currency = "USD",
        paid_date = "",
        paid_by = 0,
        settlements = emptyList<SettlementsDto>()
    ).let { MutableLiveData(it) }

    var totalAmount: Double = 0.0
    private var checkCnt: Int = 0

    private fun isValidate() = with(paramList.value!!) {
        _isValidated.value = title != "" &&
                category != "" &&
                currency != "" &&
                paid_date != "" &&
                paid_by != 0 &&
                settlements.isNotEmpty()
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

    fun getBillingMembers(id: Int, token: String) {
        viewModelScope.launch {
            getBillingMembersUseCase.invoke(id, token).let {
                _members.value = it
                _temp.value = it.map { member ->
                    InputCostEntity(
                        member.id!!,
                        false,
                        member.name!!,
                        0.0
                    )
                }
            }
        }
    }

    fun postBillings(id: Int, token: String, imgList: List<FileResult>) {
        viewModelScope.launch {
            postBillingsUseCase.invoke(id, token, paramList.value!!, imgList)
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

