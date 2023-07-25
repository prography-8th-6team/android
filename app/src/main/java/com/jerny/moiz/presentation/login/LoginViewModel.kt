package com.jerny.moiz.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.domain.usecase.LoginByKakaoTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class LoginViewModel @Inject constructor(
    private val loginByKakaoTokenUseCase: LoginByKakaoTokenUseCase,
) : ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> = _userId

    fun getKakaoToken(token: String) {
        viewModelScope.launch {
            loginByKakaoTokenUseCase.invoke(token).let {
                _token.value = it.results?.token!!
                _userId.value = it.results?.user_id!!
            }
        }
    }

}