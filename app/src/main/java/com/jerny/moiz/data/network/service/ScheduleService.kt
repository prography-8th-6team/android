package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @GET("travels/{travel_pk}/schedules")
    suspend fun getScheduleList(
        @Header("Authorization") token:String?,
        @Path("travel_pk") id:String,
        @Query("type") type:String?,
        @Query("date") date:String?,
    ):Response<ResponseScheduleListDto>
}