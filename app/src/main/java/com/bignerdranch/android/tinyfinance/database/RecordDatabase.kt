package com.bignerdranch.android.tinyfinance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.tinyfinance.data.Record

//Declare database
@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun recordDao(): RecordDao
}