package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
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

    @Multipart
    @POST("travels/{travel_pk}/schedules")
    suspend fun postTravelSchedule(
        @Header("Authorization") token: String?,
        @Path("travel_pk") id: String,
        @PartMap data: HashMap<String, RequestBody>,
        @Part img: List<MultipartBody.Part>? = null
    )

}