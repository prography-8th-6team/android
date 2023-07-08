package com.example.moiz.presentation.detail.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.TravelDetailDto
import com.example.moiz.domain.usecase.GetTravelDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getTravelDetailUseCase: GetTravelDetailUseCase,
) :
    ViewModel() {

    private val _list = MutableLiveData<TravelDetailDto>()
    val list: LiveData<TravelDetailDto> = _list

    fun getTravelDetail(id: Int, token: String) {
        viewModelScope.launch {
            getTravelDetailUseCase.invoke(id, token).let {
                _list.value = it
            }
        }
    }
}

