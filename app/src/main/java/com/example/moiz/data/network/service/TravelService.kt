package com.example.moiz.data.network.service

import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.data.network.dto.TravelDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TravelService {

    // 여행 전체 리스트 API
    @GET("v1/travels")
    suspend fun getTravelList(
        @Header("Authorization") token: String?,
    ): Response<ResponseTravelListDto>

    // 여행 생성 API
    @POST("v1/travels")
    suspend fun postTravel(
        @Body data: TravelCreateDto,
        @Header("Authorization") token: String?,
    ): Response<ResponseTravelCreateDto>

    // 여행 상세 API
    @GET("v1/travel/{id}")
    suspend fun getTravelDetail(@Path("id") id: Int): Response<TravelDto>

    // 여행 수정 API
    @PUT("v1/travels/{id}")
    suspend fun putTravel(
        @Header("Authorization") token: String?,
        @Body data: TravelCreateDto,
        @Path("id") id: Int,
    ): Response<ResponseTravelCreateDto>

    // 여행 삭제 API
    @DELETE("v1/travel/{id}")
    suspend fun deleteTravel(@Path("id") id: Int)
}