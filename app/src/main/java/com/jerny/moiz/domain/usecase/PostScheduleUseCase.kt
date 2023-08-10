package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.domain.repository.ScheduleRepository
import com.jerny.moiz.presentation.util.FileResult
import java.util.IdentityHashMap
import javax.inject.Inject

class PostScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(
        token: String,
        travelId: Int,
        data: PostScheduleDto,
        imgList: List<FileResult>?
    ) =
        repository.postSchedule(token, travelId, data, imgList)
}