package com.bignerdranch.android.tinyfinance.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.tinyfinance.data.Record
import java.util.concurrent.Executors

class RecordRepository private constructor(context: Context) {

    //Build database
    private val database: RecordDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecordDatabase::class.java,
        "record-database"
    ).build()

    private val recordDao = database.recordDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllRecord(): LiveData<List<Record>> = recordDao.loadRecord()

    fun getRecord(id: Int): LiveData<Record?> = recordDao.loadSingleRecord(id)

    fun getRecordByDate(startDate: String, endDate: String): LiveData<List<Record>> = recordDao.loadRecordByDate(startDate, endDate)

    fun addNewRecord(record: Record){
        executor.execute{
            recordDao.addRecord(record)
        }
    }

    fun updateExistingRecord(record: Record){
        executor.execute{
            recordDao.updateRecord(record)
        }
    }

    fun deleteExistingRecord(record: Record){
        executor.execute{
            recordDao.deleteRecord(record)
        }
    }

    companion object{
        private var INSTANCE: RecordRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null)
            {
                INSTANCE = RecordRepository(context)
            }
        }

        fun get(): RecordRepository{
            return INSTANCE?:
                    throw IllegalStateException("Not initialized")
        }
    }
}