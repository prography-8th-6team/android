package com.example.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class TravelDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("updated")
    val updated: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("start_date")
    val start_date: String?,
    @SerializedName("end_date")
    val end_date: String?,
    @SerializedName("user")
    val user: Int?,
    @SerializedName("members")
    val members: List<Int>?
)