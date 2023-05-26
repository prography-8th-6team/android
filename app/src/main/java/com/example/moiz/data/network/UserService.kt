package com.example.moiz.data.network

import com.example.moiz.data.network.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("v1/users/auth/kakao")
    suspend fun sendKakaoToken(@Body access_token: String): UserResponseDto

}