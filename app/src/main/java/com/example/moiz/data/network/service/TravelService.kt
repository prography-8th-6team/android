package com.example.moiz.data.network.service

import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelDeleteDto
import com.example.moiz.data.network.dto.ResponseTravelDetailDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelCreateDto
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
    @GET("v1/travels/{id}")
    suspend fun getTravelDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Response<ResponseTravelDetailDto>

    // 비용 추가 멤버
    @GET("v1/travels/{id}/members")
    suspend fun getBillingMembers(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Response<List<BillingMembersDto>>

    // 여행 수정 API
    @PUT("v1/travels/{id}")
    suspend fun putTravel(
        @Header("Authorization") token: String?,
        @Body data: TravelCreateDto,
        @Path("id") id: Int,
    ): Response<ResponseTravelCreateDto>

    // 여행 삭제 API
    @DELETE("v1/travels/{id}")
    suspend fun deleteTravel(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<ResponseTravelDeleteDto>

    // 가계부 추가
    @POST("v1/travels/{id}/billings")
    suspend fun postBillings(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
        @Body data: PostBillingDto
    )
}