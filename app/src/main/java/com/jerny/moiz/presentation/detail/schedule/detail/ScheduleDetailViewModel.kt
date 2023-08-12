package com.jerny.moiz.presentation.detail.schedule.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.usecase.GetScheduleDetailUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleDetailViewModel @Inject constructor(
    private val getScheduleDetailUseCase: GetScheduleDetailUseCase
) : ViewModel() {
    private val _scheduleData = MutableLiveData<ScheduleDto?>()
    val scheduleData: LiveData<ScheduleDto?> = _scheduleData

    fun getScheduleDetail(token: String, travelId: String, id: Int) {
        viewModelScope.launch {
            getScheduleDetailUseCase.invoke(token, travelId, id).let {
                _scheduleData.value = it.results
            }
        }
    }
}