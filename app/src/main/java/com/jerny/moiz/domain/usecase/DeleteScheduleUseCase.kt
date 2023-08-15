package com.jerny.moiz.domain.usecase

import com.jerny.moiz.data.network.dto.ResponseMessage
import com.jerny.moiz.domain.repository.ScheduleRepository
import javax.inject.Inject

class DeleteScheduleUseCase @Inject constructor(
    private val repository:ScheduleRepository,
) {
    suspend operator fun invoke(
        token:String,
        travel_pk:String,
        id:String,
    ):ResponseMessage = repository.deleteSchedule(token, travel_pk, id)
}