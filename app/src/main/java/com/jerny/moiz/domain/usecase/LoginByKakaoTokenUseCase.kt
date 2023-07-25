package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.UserInfoResponseDto
import com.jerny.moiz.domain.repository.UserRepository
import javax.inject.Inject

class LoginByKakaoTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(input: String): UserInfoResponseDto = repository.loginByKakaoToken(input)
}