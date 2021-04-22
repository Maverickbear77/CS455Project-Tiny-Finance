package com.bignerdranch.android.tinyfinance

import android.app.Application
import com.bignerdranch.android.tinyfinance.database.RecordRepository

//Initialize RecordRepository
class RecordIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        RecordRepository.initialize(this)
    }
}