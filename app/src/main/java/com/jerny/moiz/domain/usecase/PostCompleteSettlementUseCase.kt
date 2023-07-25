package com.jerny.moiz.domain.usecase

import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostCompleteSettlementUseCase @Inject constructor(private val repository: TravelRepository) {
    suspend fun invoke(
        token: String,
        travelId: Int,
    ): String = repository.postCompleteSettlement(travelId, token).message
}