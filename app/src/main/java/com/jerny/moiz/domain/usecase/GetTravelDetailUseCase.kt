package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.TravelDetailDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetTravelDetailUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(id: Int, token: String): TravelDetailDto? =
        repository.getTravelDetail(id, token).results
}