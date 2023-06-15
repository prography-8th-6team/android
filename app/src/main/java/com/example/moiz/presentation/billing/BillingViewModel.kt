package com.example.moiz.presentation.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.SettlementsDto
import com.example.moiz.domain.usecase.GetBillingMembersUseCase
import com.example.moiz.domain.usecase.PostBillingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getBillingMembersUseCase: GetBillingMembersUseCase,
    private val postBillingsUseCase: PostBillingsUseCase
) :
    ViewModel() {

    private val _members = MutableLiveData<List<BillingMembersDto>>()
    val members: LiveData<List<BillingMembersDto>> = _members

    fun getBillingMembers(id: Int, token: String) {
        viewModelScope.launch {
            getBillingMembersUseCase.invoke(id, token).let {
                _members.value = it
            }
        }
    }

    fun postBillings(id: Int, token: String, data: PostBillingDto) {
        viewModelScope.launch {
            postBillingsUseCase.invoke(id, token, data)
        }
    }

}

