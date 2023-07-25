package com.jerny.moiz.domain.usecase

import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class DeleteTravelUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        token: String,
        travelId: Int,
    ): String = repository.deleteTravel(token, travelId).message
}
