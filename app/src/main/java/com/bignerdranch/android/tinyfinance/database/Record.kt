package com.bignerdranch.android.tinyfinance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Data class
@Entity(tableName = "record_table")
data class Record (@PrimaryKey (autoGenerate = true) val id: Int,
                   var shop: String = "",
                   var amount: Double = 0.0,
                   var date: String = "",
                   var category: String = "",
                   var memo: String = "")