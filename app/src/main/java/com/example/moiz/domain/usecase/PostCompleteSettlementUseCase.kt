package com.example.moiz.domain.usecase

import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostCompleteSettlementUseCase @Inject constructor(private val repository: TravelRepository) {
    suspend fun invoke(
        token: String,
        travelId: Int,
    ): String = repository.postCompleteSettlement(travelId, token).message
}