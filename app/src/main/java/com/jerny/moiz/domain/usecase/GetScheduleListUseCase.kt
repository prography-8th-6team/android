package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetScheduleListUseCase @Inject constructor(
    private val repository: ScheduleRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: String,
        type:Int
    ): List<ScheduleDto> = repository.getScheduleList(token, id, type)
}