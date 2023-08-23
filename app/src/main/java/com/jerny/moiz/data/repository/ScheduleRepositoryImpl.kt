package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.data.network.dto.ResponseScheduleDto
import com.jerny.moiz.data.network.dto.ResponseScheduleListDto
import com.jerny.moiz.data.network.service.ScheduleService
import com.jerny.moiz.domain.repository.ScheduleRepository
import com.jerny.moiz.presentation.util.FileResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(private val scheduleService: ScheduleService) :
    ScheduleRepository {
    override suspend fun getScheduleList(
        token: String,
        id: String,
        type: String?,
        date: String?,
    ): ResponseScheduleListDto {
        val result = scheduleService.getScheduleList(token, id, type, date)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseScheduleListDto(
                message = scheduleService.getScheduleList(token, id, type, date).message(),
                results = null
            )
        }
    }

    override suspend fun deleteSchedule(
        token: String,
        travel_pk: String?,
        id: String?,
    ): ResponseMessage {
        val result = scheduleService.deleteSchedule(token, travel_pk, id)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseMessage(
                message = scheduleService.deleteSchedule(token, travel_pk, id).message()
            )
        }
    }

    override suspend fun getScheduleDetail(
        token: String?,
        travel_pk: String,
        id: String,
    ): ResponseScheduleDto {
        val result = scheduleService.getScheduleDetail(token, travel_pk, id)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseScheduleDto(
                message = scheduleService.getScheduleDetail(token, travel_pk, id).message(),
                results = null
            )
        }
    }

    override suspend fun putTravelSchedule(
        token: String?,
        travelPk: String,
        id: String,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    ): ResponseScheduleDto {
        val temp = hashMapOf<String, RequestBody>()
        temp["type"] = data.type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["title"] = data.title.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["start_at"] = data.start_at.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["end_at"] = data.end_at.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["date"] = data.date.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["category"] = data.category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["description"] =
            data.description.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imgFile = imgList?.map {
            MultipartBody.Part.createFormData(
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val result = scheduleService.putTravelSchedule(token, travelPk, id, temp, imgFile)

        return if (result.isSuccessful) {
            result.body()!!
        } else {
            ResponseScheduleDto(
                message = result.message(),
                results = null
            )
        }

    }

    override suspend fun postSchedule(
        token: String,
        travelId: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    ) {
        val temp = hashMapOf<String, RequestBody>()
        temp["type"] = data.type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["title"] = data.title.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["start_at"] = data.start_at.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["end_at"] = data.end_at.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["date"] = data.date.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["category"] = data.category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        temp["description"] =
            data.description.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imgFile = imgList?.map {
            MultipartBody.Part.createFormData(
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }
        scheduleService.postTravelSchedule(token, travelId.toString(), temp, imgFile)
    }

}