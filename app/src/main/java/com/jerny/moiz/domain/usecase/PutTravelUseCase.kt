package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PutTravelUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        token: String,
        data: TravelCreateDto,
        id: Int,
    ): TravelCreateDto? = repository.putTravel(token, data, id).results
}