package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.TravelDetailDto
import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetTravelDetailUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): TravelDetailDto? =
        repository.getTravelDetail(id, token).results
}