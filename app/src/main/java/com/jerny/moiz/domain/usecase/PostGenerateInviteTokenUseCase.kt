package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ShareTokenDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostGenerateInviteTokenUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): ShareTokenDto =
        repository.postGenerateInviteToken(id, token)
}