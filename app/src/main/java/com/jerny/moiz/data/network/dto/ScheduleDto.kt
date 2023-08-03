package com.jerny.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class ScheduleDto(
    @SerializedName("travel")
    val travel:Int?,
    @SerializedName("images")
    val images:List<ScheduleImage>,
    @SerializedName("id")
    val id:Int?,
    @SerializedName("title")
    val title:String?,
    @SerializedName("description")
    val description:String?,
    @SerializedName("type")
    val type:Int?,
    @SerializedName("category")
    val category:String?,
    @SerializedName("date")
    val date:String?,
    @SerializedName("start_at")
    val start_at:String?,
    @SerializedName("end_at")
    val end_at:String?,
    @SerializedName("created")
    val created:String?,
    @SerializedName("updated")
    val updated:String?,
)

data class ScheduleImage(
    @SerializedName("image")
    val image:String?,
)

data class ResponseScheduleListDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: List<ScheduleDto>?,
)