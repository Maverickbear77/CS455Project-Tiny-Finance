package com.bignerdranch.android.tinyfinance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bignerdranch.android.tinyfinance.data.Record

@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun recordDao(): RecordDao
}