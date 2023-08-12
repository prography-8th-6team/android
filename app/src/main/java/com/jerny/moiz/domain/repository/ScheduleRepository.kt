package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto

interface ScheduleRepository {
    suspend fun getScheduleList(token: String, id: String, type: Int): ResponseScheduleListDto

    suspend fun getScheduleDetail(token: String?, travel_pk: String, id: String) : ResponseScheduleDto
}