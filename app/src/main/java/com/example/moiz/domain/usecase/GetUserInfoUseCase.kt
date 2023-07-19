package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.UserProfileDto
import com.example.moiz.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
    ): UserProfileDto = repository.getUserProfile(token, id)
}