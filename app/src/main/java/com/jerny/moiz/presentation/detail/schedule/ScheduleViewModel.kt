package com.jerny.moiz.presentation.detail.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.usecase.GetScheduleListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleListUseCase: GetScheduleListUseCase,
) : ViewModel() {
    private val _scheduleList = MutableLiveData<List<ScheduleDto>>()
    val scheduleList: LiveData<List<ScheduleDto>> = _scheduleList

    fun getScheduleList(token: String, id: String, type: Int = 2) {
        viewModelScope.launch {
            getScheduleListUseCase.invoke(token, id, type).let {
                _scheduleList.value = it.results?.schedules!!
            }
        }
    }
}