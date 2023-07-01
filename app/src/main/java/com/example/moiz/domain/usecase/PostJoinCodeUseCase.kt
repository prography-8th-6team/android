package com.example.moiz.domain.usecase

import com.example.moiz.data.network.dto.PostJoinCodeDto
import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostJoinCodeUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        token: String,
        data: String
    ) = repository.postJoinCode(token, PostJoinCodeDto(data))
}