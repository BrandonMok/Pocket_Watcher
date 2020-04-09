package com.example.pocketwatcher.entities

import androidx.room.*

@Entity(tableName = "limitation",
    foreignKeys = arrayOf(ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE)))
data class Limitation(
    @ColumnInfo(name = "user") var user: String,
    @ColumnInfo(name = "daily") var daily: String,
    @ColumnInfo(name = "weekly") var weekly: String,
    @ColumnInfo(name = "monthly") var monthly: String
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}