package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.presentation.util.FileResult

interface ScheduleRepository {
    suspend fun getScheduleList(
        token: String,
        id: String,
        type: String?,
        date: String?,
    ): ResponseScheduleListDto

    suspend fun deleteSchedule(token: String, travel_pk: String?, id: String?): ResponseMessage

    suspend fun getScheduleDetail(
        token: String?,
        travel_pk: String,
        id: String,
    ): ResponseScheduleDto

    suspend fun putTravelSchedule(
        token: String?,
        travel_pk: String,
        id: String,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    ): ResponseScheduleDto

    suspend fun postSchedule(
        token: String,
        travelId: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    )
}