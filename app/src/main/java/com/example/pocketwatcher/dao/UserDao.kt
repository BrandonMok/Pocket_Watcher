package com.example.pocketwatcher.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.pocketwatcher.entities.User

@Dao
interface UserDao {

    @Query("select * from user where username = :username")
    fun getUserByUsername(username: String): User?

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Update(onConflict = REPLACE)
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}//interface