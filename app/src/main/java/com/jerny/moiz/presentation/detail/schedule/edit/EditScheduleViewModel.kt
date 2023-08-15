package com.jerny.moiz.presentation.detail.schedule.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.domain.usecase.GetScheduleDetailUseCase
import com.jerny.moiz.domain.usecase.PutTravelScheduleUseCase
import com.jerny.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScheduleViewModel @Inject constructor(
    private val getScheduleDetailUseCase: GetScheduleDetailUseCase,
    private val putTravelScheduleUseCase: PutTravelScheduleUseCase
) : ViewModel() {
    private val _scheduleData = MutableLiveData<ScheduleDto?>()
    val scheduleData: LiveData<ScheduleDto?> = _scheduleData

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    var imageCnt: Int = 0

    private var paramList: MutableLiveData<PostScheduleDto> = PostScheduleDto(
        title = "",
        description = "",
        type = "",
        category = "",
        date = "",
        start_at = "",
        end_at = ""
    ).let { MutableLiveData(it) }

    private fun isValidate() = with(paramList.value!!) {
        _isValidated.value = when (type) {
            // type pending : 장바구니, type confirmed : 일정
            "pending" -> {
                title != ""
            }

            else -> {
                title != "" && date != "" && start_at != "" && end_at != ""
            }
        }
    }

    fun updateParam(type: Int, value: Any?) {
        when (type) {
            0 -> {
                paramList.value?.title = value as String?
                isValidate()
            }

            1 -> {
                paramList.value?.description = value as String?
                isValidate()
            }

            2 -> {
                paramList.value?.type = value as String?
                isValidate()
            }

            3 -> {
                paramList.value?.category = value as String?
                isValidate()
            }

            4 -> {
                paramList.value?.date = value as String?
                isValidate()
            }

            5 -> {
                paramList.value?.start_at = value as String?
                isValidate()
            }

            6 -> {
                paramList.value?.end_at = value as String?
                isValidate()
            }
        }
    }

    fun getScheduleDetail(token: String, travelId: String, id: Int) {
        viewModelScope.launch {
            getScheduleDetailUseCase.invoke(token, travelId, id).let {
                _scheduleData.value = it.results
                paramList.value?.title = it.results?.title
                paramList.value?.description = it.results?.description
                paramList.value?.type = it.results?.type
                paramList.value?.category = it.results?.category

                imageCnt = it.results?.images?.size ?: 0
                isValidate()
            }
        }
    }

    fun putSchedule(token: String, travelId: Int, id: Int, imgList: List<FileResult>) {
        viewModelScope.launch {
            _isSuccess.value = putTravelScheduleUseCase.invoke(
                token,
                travelId,
                id,
                paramList.value!!,
                imgList
            ).results != null
        }
    }
}