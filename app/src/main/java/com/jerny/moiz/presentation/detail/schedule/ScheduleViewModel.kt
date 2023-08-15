package com.jerny.moiz.presentation.detail.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.usecase.DeleteScheduleUseCase
import com.jerny.moiz.domain.usecase.GetScheduleListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleListUseCase:GetScheduleListUseCase,
    private val deleteScheduleUseCase:DeleteScheduleUseCase,
) : ViewModel() {
    private val _scheduleList = MutableLiveData<List<ScheduleDto>>()
    val scheduleList:LiveData<List<ScheduleDto>> = _scheduleList

    fun getScheduleList(token:String, id:String, type:String? = "confirmed", date:String) {
        viewModelScope.launch {
            getScheduleListUseCase.invoke(token, id, type, date).let {
                _scheduleList.value = it.results?.schedules!!
            }
        }
    }

    fun deleteSchedule(token:String, travel_pk:String, id:String) {
        viewModelScope.launch {
            deleteScheduleUseCase.invoke(token, travel_pk, id).let {

            }
        }
    }
}