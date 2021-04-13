package com.bignerdranch.android.tinyfinance.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.tinyfinance.data.Record

@Dao
interface RecordDao {
    @Query("SELECT * FROM record_table ORDER BY date DESC")
    fun loadRecord(): LiveData<List<Record>>

    @Query("SELECT * FROM record_table WHERE id=(:id)")
    fun loadSingleRecord(id: Int): LiveData<Record?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecord(record: Record)

    @Update
    fun updateRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)
}