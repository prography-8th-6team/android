package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostTravelUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto? = repository.postTravel(data, token)
}