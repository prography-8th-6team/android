package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class DeleteTravelUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String) =
        repository.deleteTravel(token, id)
}