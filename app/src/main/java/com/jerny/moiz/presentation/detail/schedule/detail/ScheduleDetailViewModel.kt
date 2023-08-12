package com.jerny.moiz.presentation.detail.schedule.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.usecase.GetScheduleDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val getScheduleDetailUseCase: GetScheduleDetailUseCase
) : ViewModel() {
    private val _scheduleData = MutableLiveData<ScheduleDto?>()
    val scheduleData: LiveData<ScheduleDto?> = _scheduleData

    private var paramList: MutableLiveData<PostScheduleDto> = PostScheduleDto(
        title = null,
        description = null,
        type = null,
        category = null,
        date = null,
        start_at = null,
        end_at = null
    ).let { MutableLiveData(it) }

    fun getScheduleDetail(token: String, travelId: String, id: Int) {
        viewModelScope.launch {
            getScheduleDetailUseCase.invoke(token, travelId, id).let {
                _scheduleData.value = it.results
                paramList.value?.title = it.results?.title
                paramList.value?.description = it.results?.description
                paramList.value?.type = it.results?.type
                paramList.value?.category = it.results?.category
                paramList.value?.date = it.results?.date
                paramList.value?.start_at = it.results?.start_at
                paramList.value?.end_at = it.results?.end_at
            }
        }
    }

    fun updateParam(type: String, date: String? = null, startAt: String? = null, endAt: String? = null) {
        paramList.value?.type = type
        paramList.value?.date = date
        paramList.value?.start_at = startAt
        paramList.value?.end_at = endAt


    }

}