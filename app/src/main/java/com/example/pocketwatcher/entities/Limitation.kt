package com.example.pocketwatcher.entities

import androidx.room.*

@Entity(tableName = "limitation")
data class Limitation(
    @PrimaryKey
    @ColumnInfo(name = "user") var user: String,
    @ColumnInfo(name = "daily") var daily: String,
    @ColumnInfo(name = "weekly") var weekly: String,
    @ColumnInfo(name = "monthly") var monthly: String
)