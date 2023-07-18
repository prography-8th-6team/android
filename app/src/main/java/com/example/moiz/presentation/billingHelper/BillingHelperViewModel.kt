package com.example.moiz.presentation.billingHelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.BalancePercentDto
import com.example.moiz.data.network.dto.BalancesDto
import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.domain.usecase.GetBillingMembersUseCase
import com.example.moiz.domain.usecase.GetBillingsHelperUseCase
import com.example.moiz.domain.usecase.PostCompleteSettlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class BillingHelperViewModel @Inject constructor(
    private val getBillingsHelperUseCase: GetBillingsHelperUseCase,
    private val getBillingMembersUseCase: GetBillingMembersUseCase,
    private val postCompleteSettlementUseCase: PostCompleteSettlementUseCase,
) : ViewModel() {
    private val _balancePercent = MutableLiveData<List<BalancePercentDto>>()
    val balancePercent: LiveData<List<BalancePercentDto>> = _balancePercent

    private val _balances = MutableLiveData<List<BalancesDto>>()
    val balances: LiveData<List<BalancesDto>> = _balances

    private val _members = MutableLiveData<List<BillingMembersDto>>()
    val members: LiveData<List<BillingMembersDto>> = _members

    private val _completeResponse = MutableLiveData<String>()
    val completeResponse: LiveData<String> = _completeResponse

    fun getBillingsHelper(travelId: Int, token: String) {
        viewModelScope.launch {
            getBillingsHelperUseCase.invoke(travelId, token).let {
                _balancePercent.value = it?.balance_percent
                _balances.value = it?.balances
            }
        }
    }

    fun getBillingMembers(id: Int, token: String) {
        viewModelScope.launch {
            getBillingMembersUseCase.invoke(id, token).let {
                _members.value = it
            }
        }
    }

    fun postCompleteSettlement(token: String, id: Int) {
        viewModelScope.launch {
            postCompleteSettlementUseCase.invoke(token, id).let {
                _completeResponse.value = it
            }
        }
    }
}