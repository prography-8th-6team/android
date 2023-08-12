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
        return if (scheduleService.getScheduleList(token, id, type, date).isSuccessful) {
            scheduleService.getScheduleList(token, id, type, date).body()!!
        } else {
            ResponseScheduleListDto(
                message = scheduleService.getScheduleList(token, id, type, date).message(),
                results = null)
        }
    }

    override suspend fun deleteSchedule(
        token: String,
        travel_pk: String?,
        id: String?,
    ): ResponseMessage {
        return if (scheduleService.deleteSchedule(token, travel_pk, id).isSuccessful) {
            scheduleService.deleteSchedule(token, travel_pk, id).body()!!
        } else {
            ResponseMessage(
                message = scheduleService.deleteSchedule(token, travel_pk, id).message())
        }
    }

    override suspend fun getScheduleDetail(
        token: String?,
        travel_pk: String,
        id: String,
    ): ResponseScheduleDto {
        return if (scheduleService.getScheduleDetail(token, travel_pk, id).isSuccessful) {
            scheduleService.getScheduleDetail(token, travel_pk, id).body()!!
        } else {
            ResponseScheduleDto(
                message = scheduleService.getScheduleDetail(token, travel_pk, id).message(),
                results = null)
        }
    }

    override suspend fun putTravelSchedule(
        token: String?,
        travel_pk: String,
        id: String,
        data: PostScheduleDto,
        imgList: List<FileResult>?,
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
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull()))
        }

        scheduleService.putTravelSchedule(token, travel_pk, id, temp, imgFile)
    }

    override suspend fun postSchedule(
        token: String,
        travelId: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>?,
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
                "images", it.file.name, it.file.asRequestBody("image/*".toMediaTypeOrNull()))
        }
        scheduleService.postTravelSchedule(token, travelId.toString(), temp, imgFile)
    }

}