package com.example.moiz.data.repository

import com.example.moiz.data.network.dto.BillingDetailDto
import com.example.moiz.data.network.dto.BillingMembersDto
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.PostJoinCodeDto
import com.example.moiz.data.network.dto.ResponseBillingHelper
import com.example.moiz.data.network.dto.ResponseTravelCreateDto
import com.example.moiz.data.network.dto.ResponseTravelDeleteDto
import com.example.moiz.data.network.dto.ResponseTravelDetailDto
import com.example.moiz.data.network.dto.ResponseTravelListDto
import com.example.moiz.data.network.dto.ShareTokenDto
import com.example.moiz.data.network.dto.TravelCreateDto
import com.example.moiz.data.network.service.TravelService
import com.example.moiz.domain.repository.TravelRepository
import com.example.moiz.presentation.util.FileResult
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
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

    override suspend fun postJoinCode(token: String, data: PostJoinCodeDto) {
        travelService.postJoinCode(token, data)
    }

    override suspend fun getTravelDetail(travelId: Int, token: String): ResponseTravelDetailDto {
        return if (travelService.getTravelDetail(token, travelId).isSuccessful) {
            travelService.getTravelDetail(token, travelId).body()!!
        } else {
            ResponseTravelDetailDto(
                message = travelService.getTravelDetail(token, travelId).message(), results = null)
        }
    }

    override suspend fun getBillingDetail(billingId: String, token: String): BillingDetailDto {
        val result = travelService.getBillingDetail(token, billingId)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            Timber.d(result.message())
            BillingDetailDto(
                id = null,
                travel = null,
                title = null,
                category = null,
                paid_by = null,
                paid_date = null,
                total_amount = null,
                total_amount_currency = null,
                captured_amount = null,
                images = null,
                participants = null)
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

    override suspend fun deleteTravel(token: String, travelId: Int): ResponseTravelDeleteDto {
        return travelService.deleteTravel(token, travelId).body()!!
    }

    override suspend fun getBillingMembers(travelId: Int, token: String): List<BillingMembersDto> {
        return if (travelService.getBillingMembers(token, travelId).isSuccessful) {
            travelService.getBillingMembers(token, travelId).body()!!
        } else {
            emptyList()
        }
    }

    override suspend fun postBillings(
        travelId: Int,
        token: String,
        data: PostBillingDto,
        imgList: List<FileResult>?,
    ) {
        val temp = hashMapOf<String, RequestBody>()
        temp["title"] = data.title!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["paid_by"] = data.paid_by.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["paid_date"] = data.paid_date!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["currency"] = data.currency!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["category"] = data.category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["settlements"] =
            data.settlements.map { Gson().toJson(it).toString() }
                .toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val imgFile = imgList?.map {
            MultipartBody.Part.createFormData(
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull()))
        }

        travelService.postBillings(token, travelId, temp, imgFile)
    }

    override suspend fun getBillingsHelper(travelId: Int, token: String): ResponseBillingHelper {
        return if (travelService.getBillingsHelper(token, travelId).isSuccessful) {
            travelService.getBillingsHelper(token, travelId).body()!!
        } else {
            ResponseBillingHelper(
                message = travelService.getBillingsHelper(token, travelId).message(),
                results = null)
        }
    }

    override suspend fun putBillings(
        billingId: Int,
        token: String,
        data: PostBillingDto,
        imgList: List<FileResult>?,
    ) {
        val temp = hashMapOf<String, RequestBody>()
        temp["title"] = data.title!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["paid_by"] = data.paid_by.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["paid_date"] = data.paid_date!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["currency"] = data.currency!!.toRequestBody("text/plain".toMediaTypeOrNull())
        temp["category"] = data.category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["settlements"] =
            data.settlements.map { Gson().toJson(it).toString() }
                .toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val imgFile = imgList?.map {
            MultipartBody.Part.createFormData(
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull()))
        }

        travelService.putBillings(token, billingId, temp, imgFile)
    }

    override suspend fun postGenerateInviteToken(travelId: Int, token: String): ShareTokenDto {
        val result = travelService.postGenerateInviteToken(token, travelId.toString())
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            Timber.d(result.message())
            ShareTokenDto(message = result.message(), toekn = null)
        }
    }

}