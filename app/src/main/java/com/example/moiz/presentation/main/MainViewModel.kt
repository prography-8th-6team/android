package com.example.moiz.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moiz.domain.model.Journey

class MainViewModel : ViewModel() {
   val journeyList = MutableLiveData<List<Journey>>()
}