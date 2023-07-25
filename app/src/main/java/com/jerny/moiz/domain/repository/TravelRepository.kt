package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.BillingDetailDto
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.PostJoinCodeDto
import com.jerny.moiz.data.network.dto.ResponseBillingHelper
import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseTravelCreateDto
import com.jerny.moiz.data.network.dto.ResponseTravelDetailDto
import com.jerny.moiz.data.network.dto.ResponseTravelListDto
import com.jerny.moiz.data.network.dto.ShareTokenDto
import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.presentation.util.FileResult

interface TravelRepository {

    suspend fun getTravelList(token: String): ResponseTravelListDto

    suspend fun postTravel(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto

    suspend fun postJoinCode(
        token: String,
        data: PostJoinCodeDto
    )

    suspend fun getTravelDetail(travelId: Int, token: String): ResponseTravelDetailDto

    suspend fun getBillingDetail(billingId: String, token: String): BillingDetailDto

    suspend fun putTravel(
        token: String,
        data: TravelCreateDto,
        id: Int,
    ): ResponseTravelCreateDto

    suspend fun deleteTravel(token: String, travelId: Int): ResponseMessage

    suspend fun getBillingMembers(travelId: Int, token: String): List<BillingMembersDto>

    suspend fun postBillings(
        travelId: Int,
        token: String,
        data: PostBillingDto,
        imgList: List<FileResult>?
    )

    suspend fun putBillings(
        billingId: Int,
        token: String,
        data: PostBillingDto,
        imgList: List<FileResult>?
    )

    suspend fun postGenerateInviteToken(travelId: Int, token: String): ShareTokenDto

    suspend fun getBillingsHelper(travelId: Int, token: String): ResponseBillingHelper

    suspend fun postCompleteSettlement(travelId: Int, token: String): ResponseMessage
}