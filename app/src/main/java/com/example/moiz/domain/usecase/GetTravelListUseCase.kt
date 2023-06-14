package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetTravelListUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(token: String): ResponseTravelListDto? = repository.getTravelList(
        token)
}