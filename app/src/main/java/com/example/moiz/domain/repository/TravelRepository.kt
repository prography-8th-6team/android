package com.example.moiz.domain.repository

import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.data.network.dto.TravelDto

interface TravelRepository {

    suspend fun getTravelList(token: String): ResponseTravelListDto

    suspend fun postTravel(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto

    suspend fun getTravelDetail(travelId: Int): TravelDto

    suspend fun putTravel(travelId: Int)

    suspend fun deleteTravel(travelId: Int)

}