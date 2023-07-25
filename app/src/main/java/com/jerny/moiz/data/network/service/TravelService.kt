package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.BillingDetailDto
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.data.network.dto.PostJoinCodeDto
import com.jerny.moiz.data.network.dto.ResponseBillingHelper
import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseTravelCreateDto
import com.jerny.moiz.data.network.dto.ResponseTravelDetailDto
import com.jerny.moiz.data.network.dto.ResponseTravelListDto
import com.jerny.moiz.data.network.dto.ShareTokenDto
import com.jerny.moiz.data.network.dto.TravelCreateDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface TravelService {

    // 여행 전체 리스트 API
    @GET("travels")
    suspend fun getTravelList(
        @Header("Authorization") token: String?,
    ): Response<ResponseTravelListDto>

    // 여행 생성 API
    @POST("travels")
    suspend fun postTravel(
        @Body data: TravelCreateDto,
        @Header("Authorization") token: String?,
    ): Response<ResponseTravelCreateDto>

    // 여행 상세 API
    @GET("travels/{id}")
    suspend fun getTravelDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<ResponseTravelDetailDto>

    // 비용 추가 멤버
    @GET("travels/{id}/members")
    suspend fun getBillingMembers(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<List<BillingMembersDto>>

    // 여행 수정 API
    @PUT("travels/{id}")
    suspend fun putTravel(
        @Header("Authorization") token: String?,
        @Body data: TravelCreateDto,
        @Path("id") id: Int,
    ): Response<ResponseTravelCreateDto>

    @POST("travels/join")
    suspend fun postJoinCode(
        @Header("Authorization") token: String?,
        @Body data: PostJoinCodeDto,
    )

    @GET("billings/{id}")
    suspend fun getBillingDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: String,
    ): Response<BillingDetailDto>

    // 여행 삭제 API
    @DELETE("travels/{id}")
    suspend fun deleteTravel(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<ResponseMessage>

    @Multipart
    @PUT("billings/{id}")
    suspend fun putBillings(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
        @PartMap data: HashMap<String, RequestBody>,
        @Part img: List<MultipartBody.Part>? = null,
    )

    // 가계부 추가
    @Multipart
    @POST("travels/{id}/billings")
    suspend fun postBillings(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
        @PartMap data: HashMap<String, RequestBody>,
        @Part img: List<MultipartBody.Part>? = null,
    )

    @POST("travels/{id}/generate-invite-token")
    suspend fun postGenerateInviteToken(
        @Header("Authorization") token: String?,
        @Path("id") id: String,
    ): Response<ShareTokenDto>

    // 계산도우미 조회
    @GET("travels/{id}/billings")
    suspend fun getBillingsHelper(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<ResponseBillingHelper>

    @POST("travels/{id}/complete-settlement")
    suspend fun postCompleteSettlement(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<ResponseMessage>
}