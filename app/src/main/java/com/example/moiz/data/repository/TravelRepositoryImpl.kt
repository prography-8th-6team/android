package com.example.moiz.data.repository

import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.data.network.service.TravelService
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(private val travelService: TravelService) :
    TravelRepository {

    override suspend fun getTravelList(): List<TravelDto> {
        return travelService.getTravelList().body()!!
    }

    override suspend fun postTravel() {

    }

    override suspend fun getTravelDetail(travelId: Int): TravelDto {
        return travelService.getTravelDetail(travelId).body()!!
    }

    override suspend fun putTravel(travelId: Int) {

    }

    override suspend fun deleteTravel(travelId: Int) {

    }
}