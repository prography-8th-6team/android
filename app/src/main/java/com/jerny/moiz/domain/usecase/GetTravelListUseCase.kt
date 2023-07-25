package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ResponseTravelListDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class GetTravelListUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(token: String): ResponseTravelListDto? = repository.getTravelList(
        token)
}