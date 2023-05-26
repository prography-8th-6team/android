package com.example.moiz.domain.model

data class Journey(
   val title: String,
   val startDate: String,
   val endDate: String,
   val memberList: ArrayList<String>,
) : java.io.Serializable
