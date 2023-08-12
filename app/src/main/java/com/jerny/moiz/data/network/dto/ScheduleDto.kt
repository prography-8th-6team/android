package com.jerny.moiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class ScheduleListDto(
    @SerializedName("id")
    val id:Int?,
    @SerializedName("description")
    val description:String?,
    @SerializedName("members")
    val members: List<String>?,
    @SerializedName("title")
    val title:String?,
    @SerializedName("start_date")
    val start_date:String?,
    @SerializedName("end_date")
    val end_date:String?,
    @SerializedName("schedules")
    val schedules:List<ScheduleDto>?
)

data class ScheduleDto(
    @SerializedName("travel")
    val travel:Int?,
    @SerializedName("images")
    val images:List<String?>,
    @SerializedName("id")
    val id:Int?,
    @SerializedName("title")
    val title:String?,
    @SerializedName("description")
    val description:String?,
    @SerializedName("type")
    val type:String?,
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
data class ResponseScheduleListDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: ScheduleListDto?,
)

data class ResponseScheduleDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: ScheduleDto?,
)

data class PostScheduleDto(
    @SerializedName("type")
    var type: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("start_at")
    var start_at: String?,
    @SerializedName("end_at")
    var end_at: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("category")
    var category: String?
)