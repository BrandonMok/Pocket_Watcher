package com.example.pocketwatcher.entities

import androidx.room.*

@Entity(tableName = "expense")
data class Expense(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "value") var value: Double,
    @ColumnInfo(name = "tag") var tag: String?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "user") var user: String
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}