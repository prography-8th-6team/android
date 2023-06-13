package com.example.moiz.data.network.service

import com.example.moiz.data.network.dto.ResponseTravelCreateDto
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
    @GET("v1/travel")
    suspend fun getTravelList(): Response<List<TravelDto>>

    // 여행 생성 API
    @POST("v1/travels")
    suspend fun postTravel(
        @Body data: TravelCreateDto,
        @Header("Authorization") token: String?
    ): Response<ResponseTravelCreateDto>

    // 여행 상세 API
    @GET("v1/travel/{id}")
    suspend fun getTravelDetail(@Path("id") id: Int): Response<TravelDto>

    // 여행 수정 API
    @PUT("v1/travel/{id}")
    suspend fun putTravel(@Path("id") id: Int)

    // 여행 삭제 API
    @DELETE("v1/travel/{id}")
    suspend fun deleteTravel(@Path("id") id: Int)
}