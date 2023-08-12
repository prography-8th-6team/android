package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.data.network.dto.ScheduleDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
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

    @Multipart
    @PUT("travels/{travel_pk}/schedules/{id}")
    suspend fun putTravelSchedule(
        @Header("Authorization") token: String?,
        @Path("travel_pk") travel_pk: String,
        @Path("id") id: String,
        @PartMap data: HashMap<String, RequestBody>,
        @Part img: List<MultipartBody.Part>? = null
    )

    @Multipart
    @POST("travels/{travel_pk}/schedules")
    suspend fun postTravelSchedule(
        @Header("Authorization") token: String?,
        @Path("travel_pk") id: String,
        @PartMap data: HashMap<String, RequestBody>,
        @Part img: List<MultipartBody.Part>? = null
    )

}