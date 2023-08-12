package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.data.network.dto.ScheduleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @GET("travels/{travel_pk}/schedules")
    suspend fun getTravelDetail(
        @Header("Authorization") token: String?,
        @Path("travel_pk") id: String,
        @Query("type") type: Int,
    ): Response<ResponseScheduleListDto>

    @GET("travels/{travel_pk}/schedules/{id}")
    suspend fun getScheduleDetail(
        @Header("Authorization") token: String?,
        @Path("travel_pk") travel_pk: String,
        @Path("id") id: String
    ): Response<ResponseScheduleDto>
}