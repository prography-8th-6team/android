package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.data.network.service.ScheduleService
import com.jerny.moiz.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(private val scheduleService:ScheduleService) :
    ScheduleRepository {
    override suspend fun getScheduleList(
        token:String,
        id:String,
        type:String?,
        date:String?,
    ):ResponseScheduleListDto {
        return if (scheduleService.getScheduleList(token, id, type, date).isSuccessful) {
            scheduleService.getScheduleList(token, id, type, date).body()!!
        } else {
            ResponseScheduleListDto(
                message = scheduleService.getScheduleList(token, id, type, date).message(),
                results = null)
        }
    }
}