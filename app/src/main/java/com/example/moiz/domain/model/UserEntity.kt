package com.example.moiz.domain.model

import com.google.gson.annotations.SerializedName

data class UserEntity(
    val id: Int,
    val nickName: String,
    val token: String
)

data class TokenEntity(
    @SerializedName("access_token")
    val access_token: String
)

data class InputCostEntity(
    val isChecked: Boolean,
    val name: String,
    val userId: Int,
    val cost: Double
)