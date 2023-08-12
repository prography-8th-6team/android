package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetScheduleDetailUseCase @Inject constructor(
    private val repository: ScheduleRepository,
) {
    suspend operator fun invoke(
        token: String,
        travelId: String,
        id: Int,
    ): ResponseScheduleDto = repository.getScheduleDetail(token, travelId, id.toString())
}