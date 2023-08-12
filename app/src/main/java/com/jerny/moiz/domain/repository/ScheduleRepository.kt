package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.presentation.util.FileResult

interface ScheduleRepository {
    suspend fun getScheduleList(
        token:String,
        id:String,
        type:String?,
        date:String?,
    ):ResponseScheduleListDto

    suspend fun deleteSchedule(token:String, travel_pk:String?, id:String?):ResponseMessage
    suspend fun postSchedule(
        token:String,
        travelId:Int,
        data:PostScheduleDto,
        imgList:List<FileResult>?,
    )
}