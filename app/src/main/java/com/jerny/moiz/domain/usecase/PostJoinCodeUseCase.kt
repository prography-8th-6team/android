package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.PostJoinCodeDto
import com.jerny.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class PostJoinCodeUseCase @Inject constructor(
    private val repository: TravelRepository,
) {
    suspend operator fun invoke(
        token: String,
        data: String
    ) = repository.postJoinCode(token, PostJoinCodeDto(data))
}