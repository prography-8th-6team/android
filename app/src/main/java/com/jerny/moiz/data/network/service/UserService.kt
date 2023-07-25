package com.jerny.moiz.data.network.service

import com.jerny.moiz.data.network.dto.UserProfileResponseDto
import com.jerny.moiz.data.network.dto.UserInfoResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @FormUrlEncoded
    @POST("users/auth/kakao")
    suspend fun sendKakaoToken(@Field("access_token") token: String): Response<UserInfoResponseDto>

    @GET("users/{id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
    ): Response<UserProfileResponseDto>
}