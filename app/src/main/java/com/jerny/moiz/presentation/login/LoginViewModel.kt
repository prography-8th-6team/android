package com.jerny.moiz.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.UserInfoDto
import com.jerny.moiz.domain.usecase.LoginByKakaoTokenUseCase
import com.jerny.moiz.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginByKakaoTokenUseCase: LoginByKakaoTokenUseCase,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UiState<UserInfoDto>>(UiState.Loading)
    val userInfo: StateFlow<UiState<UserInfoDto>> = _userInfo

    fun getKakaoToken(token: String) {
        viewModelScope.launch {
            loginByKakaoTokenUseCase.invoke(token).let {
                _userInfo.emit(UiState.Success(it.results!!))
            }
        }
    }
}