package com.example.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class ShareTokenDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("toekn")
    val toekn: String?
)