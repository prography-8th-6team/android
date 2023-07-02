package com.example.moiz.presentation.billing.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.BillingDetailDto
import com.example.moiz.domain.usecase.GetBillingDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBillingViewModel@Inject constructor(
    private val getBillingDetailUseCase: GetBillingDetailUseCase
) :
    ViewModel() {

    private val _data = MutableLiveData<BillingDetailDto>()
    val data: LiveData<BillingDetailDto> = _data

    fun getBillingDetail(id: Int, token: String) {
        viewModelScope.launch {
            getBillingDetailUseCase.invoke(id, token).let {
                _data.value = it
            }
        }
    }
}