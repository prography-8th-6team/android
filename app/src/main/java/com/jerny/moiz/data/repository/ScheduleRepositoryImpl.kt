package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.ResponseScheduleDto
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

    override suspend fun getScheduleDetail(
        token: String?,
        travel_pk: String,
        id: String
    ): ResponseScheduleDto {
        return if (scheduleService.getScheduleDetail(token, travel_pk, id).isSuccessful) {
            scheduleService.getScheduleDetail(token, travel_pk, id).body()!!
        } else {
            ResponseScheduleDto(
                message = scheduleService.getScheduleDetail(token, travel_pk, id).message(),
                results = null)
        }
    }

}