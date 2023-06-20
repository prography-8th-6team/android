package com.example.moiz.domain.repository

import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelDeleteDto
import com.example.moiz.data.network.dto.ResponseTravelDetailDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.ShareTokenDto
import com.example.moiz.data.network.dto.TravelCreateDto

interface TravelRepository {

    suspend fun getTravelList(token: String): ResponseTravelListDto

    suspend fun postTravel(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto

    suspend fun getTravelDetail(travelId: Int, token: String): ResponseTravelDetailDto

    suspend fun putTravel(
        token: String,
        data: TravelCreateDto,
        id: Int,
    ): ResponseTravelCreateDto

    suspend fun deleteTravel(token: String, travelId: Int): ResponseTravelDeleteDto

    suspend fun getBillingMembers(travelId: Int, token: String): List<BillingMembersDto>

    suspend fun postBillings(travelId: Int, token: String, data: PostBillingDto)

    suspend fun postGenerateInviteToken(travelId: Int, token: String): ShareTokenDto
}