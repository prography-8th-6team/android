package com.example.moiz.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiz.data.network.dto.KakaoToken
import com.example.moiz.domain.usecase.LoginByKakaoTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginByKakaoTokenUseCase: LoginByKakaoTokenUseCase
) :
    ViewModel() {

    private val _token = MutableLiveData<KakaoToken>()
    val token: LiveData<KakaoToken> = _token

    fun getKakaoToken(token: String) {
        viewModelScope.launch {
            loginByKakaoTokenUseCase.invoke(token).let{
                _token.value = it.results!!
            }
        }
    }

}