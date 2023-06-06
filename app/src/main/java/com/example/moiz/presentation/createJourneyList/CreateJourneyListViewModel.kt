package com.example.moiz.presentation.createJourneyList

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateJourneyListViewModel : ViewModel() {
    val calendar = Calendar.getInstance()
    val format = SimpleDateFormat("yyy.MM.dd")

    val title = MutableLiveData<String>()
    val startDate = MutableLiveData(format.format(calendar.time))
    val endDate = MutableLiveData(format.format(calendar.time))
    val color = MutableLiveData<String>()
    val currency = MutableLiveData<String>()
    val memo = MutableLiveData<String>()
    val isEnabled = MutableLiveData(false)

    val titleCount = MutableLiveData(0)
    val memoCount = MutableLiveData(0)

    init {
        viewModelScope.launch { title.asFlow().collect { checkValidate() } }
        viewModelScope.launch { startDate.asFlow().collect { checkValidate() } }
        viewModelScope.launch { endDate.asFlow().collect { checkValidate() } }
        viewModelScope.launch { color.asFlow().collect { checkValidate() } }
        viewModelScope.launch { currency.asFlow().collect { checkValidate() } }
    }

    private fun checkValidate() {
        isEnabled.value =
            !title.value.isNullOrBlank() && !startDate.value.isNullOrBlank() && !endDate.value.isNullOrBlank() && !color.value.isNullOrBlank() && !currency.value.isNullOrBlank()
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
}