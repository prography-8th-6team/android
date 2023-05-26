package com.example.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: KakaoToken?
)

data class KakaoToken(
    @SerializedName("token")
    val token: Int
)