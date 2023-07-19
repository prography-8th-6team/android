package com.example.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserInfoResponseDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: UserInfoDto?,
)

data class UserInfoDto(
    @SerializedName("token")
    val token: String?,
    @SerializedName("refresh_token")
    val refresh_token: String?,
    @SerializedName("user_id")
    val user_id: Int?,
    @SerializedName("nickname")
    val nickname: String?,
)

data class UserProfileResponseDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: UserProfileDto?,
)

data class UserProfileDto(
    @SerializedName("nickname")
    val nickname:String?,
    @SerializedName("fcm_token")
    val fcm_token:String?,
    @SerializedName("created")
    val created:String?,
)