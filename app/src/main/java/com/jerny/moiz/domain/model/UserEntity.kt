package com.jerny.moiz.domain.model

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
    val userId: Int,
    var isChecked: Boolean,
    val name: String,
    var cost: Double
)