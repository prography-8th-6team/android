package com.example.moiz.domain.repository

import com.example.moiz.data.network.dto.TravelDto

interface TravelRepository {

    suspend fun getTravelList(): List<TravelDto>

    suspend fun postTravel()

    suspend fun getTravelDetail(travelId: Int): TravelDto

    suspend fun putTravel(travelId: Int)

    suspend fun deleteTravel(travelId: Int)

}