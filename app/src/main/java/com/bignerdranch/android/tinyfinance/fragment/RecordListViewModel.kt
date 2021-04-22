package com.bignerdranch.android.tinyfinance.fragment

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.tinyfinance.database.RecordRepository


//A class for view list
class RecordListViewModel: ViewModel() {
    private val recordRepository = RecordRepository.get()
    val recordsLiveData = recordRepository.getAllRecord()
}