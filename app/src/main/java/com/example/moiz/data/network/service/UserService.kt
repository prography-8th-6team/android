package com.example.moiz.data.network.service

import com.example.moiz.data.network.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("users/auth/kakao")
    suspend fun sendKakaoToken(@Field("access_token") token: String): Response<UserResponseDto>
}