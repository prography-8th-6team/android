package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ResponseTravelCreateDto
import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostTravelUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto? = repository.postTravel(data, token)
}