package com.jerny.moiz.presentation.detail.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.data.network.dto.PostBillingDto
import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.data.network.dto.SettlementsDto
import com.jerny.moiz.domain.model.InputCostEntity
import com.jerny.moiz.domain.usecase.PostScheduleUseCase
import com.jerny.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val postScheduleUseCase: PostScheduleUseCase
) :
    ViewModel() {

    private val _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    private var paramList: MutableLiveData<PostScheduleDto> = PostScheduleDto(
        travel = 0,
        title = "",
        description = "",
        type = 0,
        category = "",
        date = "",
        start_at = "",
        end_at = ""
    ).let { MutableLiveData(it) }

    private fun isValidate() = with(paramList.value!!) {
        _isValidated.value = when (type) {
            // type 1 : 장바구니, type 2 : 일정
            1 -> {
                title != "" && description != "" && category != ""
            }

            else -> {
                title != "" && description != "" && category != "" && date != "" && start_at != "" && end_at != ""
            }
        }
    }

    fun updateParam(type: Int, value: Any?) {
        when (type) {
            0 -> {
                paramList.value?.travel = value as Int?
                isValidate()
            }

            1 -> {
                paramList.value?.title = value as String?
                isValidate()
            }

            2 -> {
                paramList.value?.description = value as String?
                isValidate()
            }

            3 -> {
                paramList.value?.type = value as Int?
                isValidate()
            }

            4 -> {
                paramList.value?.category = value as String?
                isValidate()
            }

            5 -> {
                paramList.value?.date = value as String?
                isValidate()
            }

            6 -> {
                paramList.value?.start_at = value as String?
                isValidate()
            }

            7 -> {
                paramList.value?.end_at = value as String?
                isValidate()
            }
        }
    }

    fun postSchedule(id: Int, token: String, imgList: List<FileResult>) {
        viewModelScope.launch {
            postScheduleUseCase.invoke(id, token, paramList.value!!, imgList)
        }
    }
}