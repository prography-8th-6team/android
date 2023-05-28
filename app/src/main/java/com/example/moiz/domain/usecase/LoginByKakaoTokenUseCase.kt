package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.domain.repository.UserRepository
import javax.inject.Inject

class LoginByKakaoTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(input: String): UserResponseDto = repository.loginByKakaoToken(input)
}