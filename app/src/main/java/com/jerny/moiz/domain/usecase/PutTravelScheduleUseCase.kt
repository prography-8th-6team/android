package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.domain.repository.ScheduleRepository
import com.jerny.moiz.presentation.util.FileResult
import javax.inject.Inject

class PutTravelScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(
        token: String,
        travelId: Int,
        id: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>? = null
    ) =
        repository.putTravelSchedule(token, travelId.toString(), id.toString(), data, imgList)
}