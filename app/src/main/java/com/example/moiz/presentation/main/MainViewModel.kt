package com.example.moiz.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.domain.usecase.GetTravelListUseCase
import com.example.moiz.domain.usecase.GetUserInfoUseCase
import com.example.moiz.domain.usecase.PostJoinCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
    private val getTravelListUseCase: GetTravelListUseCase,
    private val postJoinCodeUseCase: PostJoinCodeUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _response = MutableLiveData<ResponseTravelListDto>()
    val response: LiveData<ResponseTravelListDto> = _response

    private val _list = MutableLiveData<List<TravelDto>>()
    val list: LiveData<List<TravelDto>> = _list

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> = _nickName

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

    fun postJoinCode(token: String, joinCode: String) {
        viewModelScope.launch {
            postJoinCodeUseCase.invoke(token, joinCode).let {

            }
        }
    }

    fun getUserProfile(token: String, id: Int) {
        viewModelScope.launch {
            getUserInfoUseCase.invoke(token, id).let {
                _nickName.value = it.nickname!!
            }
        }
    }
}