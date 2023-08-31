package com.jerny.moiz.presentation.detail.schedule.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.PostScheduleDto
import com.jerny.moiz.domain.usecase.PostScheduleUseCase
import com.jerny.moiz.presentation.util.FileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val postScheduleUseCase: PostScheduleUseCase
) :
    ViewModel() {

    var title = MutableStateFlow("")
    var category = MutableStateFlow("")
    var description = MutableStateFlow("")
    var date = MutableStateFlow("")
    var startAt = MutableStateFlow("")
    var endAt = MutableStateFlow("")
    var type = MutableStateFlow("")

    var totalValidation: Flow<Boolean>
        get() = if (type.value == "pending") {
            wishListValidated
        } else {
            combine(wishListValidated, scheduleValidate) { wishListValidated, scheduleValidate ->
                wishListValidated && scheduleValidate
            }
        }
        set(_) {}
    
    private var wishListValidated: Flow<Boolean> = combine(
        title, category
    ) { title, _ -> title != "" }

    private var scheduleValidate: Flow<Boolean> = combine(
        date, startAt, endAt
    ) { date, startAt, endAt ->
        date != "" && startAt != "" && endAt != ""
    }

    fun updateParam(idx: Int, value: Any?) {
        when (idx) {
            0 -> title.value = value as String
            1 -> description.value = value as String
            2 -> type.value = value as String
            3 -> category.value = value as String
            4 -> date.value = value as String
            5 -> startAt.value = value as String
            6 -> endAt.value = value as String
        }
    }

    fun postSchedule(token: String, travelId: Int, imgList: List<FileResult>) {
        viewModelScope.launch {
            postScheduleUseCase.invoke(
                token,
                travelId,
                PostScheduleDto(
                    type.value,
                    title.value,
                    description.value,
                    startAt.value,
                    endAt.value,
                    date.value,
                    category.value
                ),
                imgList
            )
        }
    }
}