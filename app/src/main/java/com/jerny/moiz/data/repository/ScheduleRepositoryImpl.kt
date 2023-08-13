package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.ResponseMessage
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

    override suspend fun deleteSchedule(
        token:String,
        travel_pk:String?,
        id:String?,
    ):ResponseMessage {
        return if (scheduleService.deleteSchedule(token, travel_pk, id).isSuccessful) {
            scheduleService.deleteSchedule(token, travel_pk, id).body()!!
        } else {
            ResponseMessage(
                message = scheduleService.deleteSchedule(token, travel_pk, id).message())
        }
    }
}