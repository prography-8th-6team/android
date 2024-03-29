package com.jerny.moiz.data.repository

import com.google.gson.Gson
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
import com.jerny.moiz.data.network.service.TravelService
import com.jerny.moiz.domain.repository.TravelRepository
import com.jerny.moiz.presentation.util.FileResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

class TravelRepositoryImpl @Inject constructor(private val travelService: TravelService) :
    TravelRepository {

    override suspend fun getTravelList(token: String): ResponseTravelListDto {
        val result = travelService.getTravelList(token)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseTravelListDto(
                message = result.message(), results = null
            )
        }

    }

    override suspend fun postTravel(
        data: TravelCreateDto,
        token: String,
    ): ResponseTravelCreateDto {
        val result = travelService.postTravel(data, token)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseTravelCreateDto(
                message = result.message(), results = null
            )
        }
    }

    override suspend fun postJoinCode(token: String, data: PostJoinCodeDto) {
        try {
            travelService.postJoinCode(token, data)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getTravelDetail(travelId: Int, token: String): ResponseTravelDetailDto {
        val result = travelService.getTravelDetail(token, travelId)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseTravelDetailDto(
                message = result.message(), results = null
            )
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
                participants = null
            )
        }
    }

    override suspend fun putTravel(
        token: String,
        data: TravelCreateDto,
        id: Int,
    ): ResponseTravelCreateDto {
        val result = travelService.putTravel(token, data, id)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseTravelCreateDto(
                message = result.message(), results = null
            )
        }
    }

    override suspend fun deleteTravel(token: String, travelId: Int): ResponseMessage {
        return travelService.deleteTravel(token, travelId).body()!!
    }

    override suspend fun getBillingMembers(travelId: Int, token: String): List<BillingMembersDto> {
        val result = travelService.getBillingMembers(token, travelId)
        return if (result.isSuccessful) {
            result.body()!!
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
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        travelService.postBillings(token, travelId, temp, imgFile)
    }

    override suspend fun getBillingsHelper(travelId: Int, token: String): ResponseBillingHelper {
        val result = travelService.getBillingsHelper(token, travelId)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseBillingHelper(
                message = result.message(),
                results = null
            )
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
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        travelService.putBillings(token, billingId, temp, imgFile)
    }

    override suspend fun postGenerateInviteToken(travelId: Int, token: String): ShareTokenDto {
        val result = travelService.postGenerateInviteToken(token, travelId.toString())
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ShareTokenDto(message = result.message(), toekn = null)
        }
    }

    override suspend fun postCompleteSettlement(travelId: Int, token: String): ResponseMessage {
        val result = travelService.postCompleteSettlement(token, travelId)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            // 400 error 발생하는 경우 body 가 empty 이기 때문에 errorBody 를 파싱
            val peekSource = result.errorBody()?.source()?.peek()
            val contentType = result.errorBody()?.contentType()
            val charset = contentType?.let { contentType.charset(UTF_8) }
            val response = peekSource?.readString(charset!!)
            val message = response?.split("\"")?.get(3)!!
            ResponseMessage(message = message)
        }
    }
}