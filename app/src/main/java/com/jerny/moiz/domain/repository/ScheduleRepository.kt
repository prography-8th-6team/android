package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.presentation.util.FileResult

interface ScheduleRepository {
    suspend fun getScheduleList(token: String, id: String, type: Int): ResponseScheduleListDto

    suspend fun postSchedule(
        token: String,
        id: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    )

}