package com.example.pocketwatcher.entities

import androidx.room.*

@Entity(tableName = "Expense")
data class Expense(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "value") var value: Double,
    @ColumnInfo(name = "tag") var tag: String?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "creator") var creator: String
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}