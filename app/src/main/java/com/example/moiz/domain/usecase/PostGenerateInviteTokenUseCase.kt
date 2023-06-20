package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.ShareTokenDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostGenerateInviteTokenUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): ShareTokenDto =
        repository.postGenerateInviteToken(id, token)
}