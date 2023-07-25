package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.UserProfileDto
import com.jerny.moiz.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
    ): UserProfileDto = repository.getUserProfile(token, id)
}