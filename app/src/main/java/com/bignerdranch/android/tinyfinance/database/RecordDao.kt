package com.bignerdranch.android.tinyfinance.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.tinyfinance.data.Record

//UserDao to interact with database
@Dao
interface RecordDao {
    //Load all records
    @Query("SELECT * FROM record_table ORDER BY date DESC")
    fun loadRecord(): LiveData<List<Record>>

    //Load record by id
    @Query("SELECT * FROM record_table WHERE id=(:id)")
    fun loadSingleRecord(id: Int): LiveData<Record?>

    //Load records by date
    @Query("SELECT * FROM record_table WHERE date BETWEEN :startDate AND :endDate" )
    fun loadRecordByDate(startDate: String, endDate: String): LiveData<List<Record>>

    //Add new record
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecord(record: Record)

    //Update an existing record
    @Update
    fun updateRecord(record: Record)

    //Delete an existing record
    @Delete
    fun deleteRecord(record: Record)
}