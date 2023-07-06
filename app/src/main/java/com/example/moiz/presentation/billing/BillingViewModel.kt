package com.example.moiz.presentation.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.BillingDto
import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.SettlementsDto
import com.example.moiz.domain.model.InputCostEntity
import com.example.moiz.domain.usecase.GetBillingMembersUseCase
import com.example.moiz.domain.usecase.PostBillingsUseCase
import com.example.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getBillingMembersUseCase: GetBillingMembersUseCase,
    private val postBillingsUseCase: PostBillingsUseCase
) :
    ViewModel() {

    private val _members = MutableLiveData<List<BillingMembersDto>>()
    val members: LiveData<List<BillingMembersDto>> = _members

    private val _temp = MutableLiveData<List<InputCostEntity>?>()
    val temp: LiveData<List<InputCostEntity>?> = _temp

    private val paramList: MutableLiveData<PostBillingDto> = PostBillingDto(
        title = "테스트1",
        paid_by = 7,
        paid_date = "2023-07-06",
        currency = "USD",
        category = "hotel",
        settlements = listOf(SettlementsDto(7, 13000.0))
    ).let { MutableLiveData(it) }

    var totalAmount: Double = 0.0
    private var checkCnt: Int = 0

    fun isValidate(): Boolean {
        return if (paramList.value?.title == "") false
        else if (paramList.value?.paid_by == 0) false
        else !paramList.value?.settlements?.isEmpty()!!
    }

    fun updateParam(type: Int, value: Any?) {
        when (type) {
            0 -> {
                if (paramList.value?.title == value as String?)
                    paramList.value?.title = value
            }

            1 -> {
                if (paramList.value?.paid_by == value as Int?)
                    paramList.value?.paid_by = value
            }

            2 -> {
                if (paramList.value?.paid_date == value as String?)
                    paramList.value?.paid_date = value
            }

            3 -> {
                if (paramList.value?.currency == value as String?)
                    paramList.value?.currency = value
            }

            4 -> {
                paramList.value?.settlements = _temp.value?.map {
                    SettlementsDto(
                        it.userId,
                        it.cost
                    )
                }!!
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
        Timber.d(paramList.value.toString())
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

        updateParam(4, null)
    }

    fun updateCost(data: InputCostEntity) {
        if (data.isChecked) checkCnt++
        else checkCnt--

        val temp = _temp.value?.toMutableList()
        temp?.forEach {
            if (it.userId == data.userId) {
                it.isChecked = data.isChecked
            }
        }
        _temp.value = temp

        updateTotalAmount()
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

        updateParam(4, null)
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

        updateParam(4, null)
    }

}

