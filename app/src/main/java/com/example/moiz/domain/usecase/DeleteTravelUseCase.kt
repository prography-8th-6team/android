package com.example.moiz.domain.usecase

import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class DeleteTravelUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        token: String,
        travelId: Int,
    ): String = repository.deleteTravel(token, travelId).message
}
