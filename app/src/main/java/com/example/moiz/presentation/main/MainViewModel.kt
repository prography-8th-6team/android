package com.example.moiz.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.domain.usecase.GetTravelListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
    private val getTravelListUseCase: GetTravelListUseCase,
) : ViewModel() {
    private val _response = MutableLiveData<ResponseTravelListDto>()
    val response: LiveData<ResponseTravelListDto> = _response

    private val _list = MutableLiveData<List<TravelDto>>()
    val list: LiveData<List<TravelDto>> = _list

    fun setTravelList(value: List<TravelDto>) {
        _list.value = value
    }

    fun getTravelList(token: String) {
        viewModelScope.launch {
            getTravelListUseCase.invoke(token).let {
                _response.value = it
            }
        }
    }
}