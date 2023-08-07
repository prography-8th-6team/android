package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.ResponseScheduleListDto

interface ScheduleRepository {
    suspend fun getScheduleList(token: String, id: String, type: Int): ResponseScheduleListDto
}