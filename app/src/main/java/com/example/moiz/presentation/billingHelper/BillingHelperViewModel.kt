package com.example.moiz.presentation.billingHelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.BalancesDto
import com.example.moiz.data.network.dto.UserAmountsDto
import com.example.moiz.domain.usecase.GetBillingsHelperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class BillingHelperViewModel @Inject constructor(
    private val getBillingsHelperUseCase: GetBillingsHelperUseCase,
) : ViewModel() {
    private val _userAmounts = MutableLiveData<List<UserAmountsDto>>()
    val userAmounts: LiveData<List<UserAmountsDto>> = _userAmounts

    private val _balances = MutableLiveData<List<BalancesDto>>()
    val balances: LiveData<List<BalancesDto>> = _balances

    fun getBillingsHelper(travelId: Int, token: String) {
        viewModelScope.launch {
            getBillingsHelperUseCase.invoke(travelId, token).let {
                _userAmounts.value = it?.user_amounts
                _balances.value = it?.balances
            }
        }
    }
}