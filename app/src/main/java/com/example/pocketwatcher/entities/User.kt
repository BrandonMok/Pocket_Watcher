package com.example.pocketwatcher.entities

import androidx.room.*

@Entity(tableName = "User")
data class User(
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}