package com.bignerdranch.android.tinyfinance.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.tinyfinance.data.Record
import com.bignerdranch.android.tinyfinance.database.RecordRepository

//A class for checking detail of a record
class RecordDetailViewModel: ViewModel() {
    private val recordRepository = RecordRepository.get()
    private val recordIdLiveData = MutableLiveData<Int>()

    var recordLiveData: LiveData<Record?> = Transformations.switchMap(recordIdLiveData){ recordId ->
        recordRepository.getRecord(recordId)
    }

    fun loadRecord(recordId: Int){
        recordIdLiveData.value = recordId
    }

    fun updateRecord(record: Record){
        recordRepository.updateExistingRecord(record)
    }

    fun deleteRecord(record: Record){
        recordRepository.deleteExistingRecord(record)
    }
}