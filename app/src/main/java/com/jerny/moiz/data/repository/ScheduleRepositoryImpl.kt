package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.data.network.service.ScheduleService
import com.jerny.moiz.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(private val scheduleService: ScheduleService) :
    ScheduleRepository {
    override suspend fun getScheduleList(
        token: String,
        id: String,
        type: Int,
    ): ResponseScheduleListDto {
        return if (scheduleService.getTravelDetail(token, id, type).isSuccessful) {
            scheduleService.getTravelDetail(token, id, type).body()!!
        } else {
            ResponseScheduleListDto(
                message = scheduleService.getTravelDetail(token, id, type).message(),
                results = null)

        }
    }
}