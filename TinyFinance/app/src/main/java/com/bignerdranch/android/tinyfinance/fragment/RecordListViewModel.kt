package com.bignerdranch.android.tinyfinance.fragment

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.tinyfinance.data.Record
import com.bignerdranch.android.tinyfinance.database.RecordRepository

class RecordListViewModel: ViewModel() {
    private val recordRepository = RecordRepository.get()
    val recordsLiveData = recordRepository.getAllRecord()
}