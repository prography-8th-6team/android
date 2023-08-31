package com.jerny.moiz.presentation.travel.edit

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.data.network.dto.TravelDetailDto
import com.jerny.moiz.domain.usecase.GetTravelDetailUseCase
import com.jerny.moiz.domain.usecase.PutTravelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class EditTravelListViewModel @Inject constructor(
    private val getTravelDetailUseCase: GetTravelDetailUseCase,
    private val putTravelUseCase: PutTravelUseCase,
) : ViewModel() {
    val title = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()
    val color = MutableLiveData<String>()
    val currency = MutableLiveData<String>()
    val memo = MutableLiveData<String>()
    val isEnabled = MutableLiveData(false)

    val titleCount = MutableLiveData(0)
    val memoCount = MutableLiveData(0)

    private val _travelDetail = MutableLiveData<TravelDetailDto>()
    val travelDetail: LiveData<TravelDetailDto> = _travelDetail

    private val _response = MutableLiveData<TravelCreateDto>()
    val response: LiveData<TravelCreateDto> = _response

    init {
        viewModelScope.launch { title.asFlow().collect { checkValidate() } }
        viewModelScope.launch { startDate.asFlow().collect { checkValidate() } }
        viewModelScope.launch { endDate.asFlow().collect { checkValidate() } }
        viewModelScope.launch { color.asFlow().collect { checkValidate() } }
        viewModelScope.launch { currency.asFlow().collect { checkValidate() } }
        viewModelScope.launch { memo.asFlow().collect { checkValidate() } }
    }

    private fun checkValidate() {
        isEnabled.value =
            (title.value != travelDetail.value?.title) || (startDate.value != travelDetail.value?.start_date) || (endDate.value != travelDetail.value?.end_date) || (color.value != travelDetail.value?.color) || (currency.value != travelDetail.value?.currency || (memo.value != travelDetail.value?.description))

    }

    fun setTitle(value: String) {
        title.value = value
    }

    fun setStartDate(date: String) {
        startDate.value = date
    }

    fun setEndDate(date: String) {
        endDate.value = date
    }

    fun setColor(value: String) {
        color.value = value
    }

    fun setCurrency(value: String) {
        currency.value = value
    }

    fun setMemo(value: String) {
        memo.value = value
    }

    val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            titleCount.value = p0?.length
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    val memoTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            memoCount.value = p0?.length
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    fun getTravelDetail(id: Int, token: String) {
        viewModelScope.launch {
            getTravelDetailUseCase.invoke(id, token).let {
                _travelDetail.value = it
            }
        }
    }

    fun putTravel(token: String, data: TravelCreateDto, id: Int) {
        viewModelScope.launch {
            putTravelUseCase.invoke(token, data, id)?.let {
                _response.value = it
            }
        }
    }
}