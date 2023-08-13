package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto

interface ScheduleRepository {
    suspend fun getScheduleList(token: String, id: String, type: String?, date:String?): ResponseScheduleListDto

    suspend fun deleteSchedule(token:String, travel_pk:String?, id:String?):ResponseMessage
}