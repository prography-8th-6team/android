package com.example.moiz.data.repository

import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.ResponseBillingHelper
import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelDetailDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.data.network.service.TravelService
import com.example.moiz.domain.repository.TravelRepository
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(private val travelService: TravelService) :
    TravelRepository {

    override suspend fun getTravelList(token: String): ResponseTravelListDto {
        return if (travelService.getTravelList(token).isSuccessful) {
            travelService.getTravelList(token).body()!!
        } else {
            ResponseTravelListDto(
                message = travelService.getTravelList(token).message(), results = null)
        }

    }

    override suspend fun postTravel(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto {
        return if (travelService.postTravel(data, token).isSuccessful) {
            travelService.postTravel(data, token).body()!!
        } else {
            ResponseTravelCreateDto(
                message = travelService.postTravel(data, token).message(), results = null)
        }
    }

    override suspend fun getTravelDetail(travelId: Int, token: String): ResponseTravelDetailDto {
        return if (travelService.getTravelDetail(token, travelId).isSuccessful) {
            travelService.getTravelDetail(token, travelId).body()!!
        } else {
            ResponseTravelDetailDto(
                message = travelService.getTravelDetail(token, travelId).message(),
                results = null
            )
        }
    }

    override suspend fun putTravel(
        token: String,
        data: TravelCreateDto,
        id: Int,
    ): ResponseTravelCreateDto {
        return if (travelService.putTravel(token, data, id).isSuccessful) {
            travelService.putTravel(token, data, id).body()!!
        } else {
            ResponseTravelCreateDto(
                message = travelService.putTravel(token, data, id).message(), results = null)
        }
    }

    override suspend fun deleteTravel(travelId: Int) {

    }

    override suspend fun getBillingMembers(travelId: Int, token: String): List<BillingMembersDto> {
        return if (travelService.getBillingMembers(token, travelId).isSuccessful) {
            travelService.getBillingMembers(token, travelId).body()!!
        } else {
            emptyList()
        }
    }

    override suspend fun postBillings(travelId: Int, token: String, data: PostBillingDto) {
        travelService.postBillings(token, travelId, data)
    }

    override suspend fun getBillingsHelper(travelId: Int, token: String): ResponseBillingHelper {
        return if (travelService.getBillingsHelper(token, travelId).isSuccessful) {
            travelService.getBillingsHelper(token, travelId).body()!!
        } else {
            ResponseBillingHelper(
                message = travelService.getBillingsHelper(token, travelId)
                    .message(), results = null)
        }
    }
}