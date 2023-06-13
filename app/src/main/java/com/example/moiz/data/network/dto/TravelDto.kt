package com.example.moiz.data.network.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class TravelDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("members")
    val members: List<String>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("start_date")
    val start_date: String?,
    @SerializedName("end_date")
    val end_date: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("total_captured_amount")
    val total_captured_amount: String?,
    @SerializedName("total_amount")
    val total_amount: String?,
    @SerializedName("my_total_billing")
    val my_total_billing: String?,
    @SerializedName("billings")
    val billings: List<BillingDto>?,
)

data class BillingDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("travel")
    val travel: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("paid_by")
    val paid_by: String?,
    @SerializedName("paid_date")
    val paid_date: String?,
    @SerializedName("total_amount")
    val total_amount: String?,
    @SerializedName("total_amount_currency")
    val total_amount_currency: String?,
    @SerializedName("captured_amount")
    val captured_amount: String?,
    @SerializedName("participants")
    val participants: List<String>?,
)

data class ResponseTravelListDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("results")
    val results: List<TravelDto>?,
)

data class TravelCreateDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("start_date")
    val start_date: String?,
    @SerializedName("end_date")
    val end_date: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("currency")
    val currency: String?,
)

data class ResponseTravelCreateDto(
    @SerializedName("message")
    val message:String,
    @SerializedName("results")
    val results:TravelCreateDto?
)