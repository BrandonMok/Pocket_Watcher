package com.example.pocketwatcher.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.pocketwatcher.entities.Limitation

@Dao
interface LimitationDao {

    @Query("SELECT * FROM limitation WHERE user = :username")
    fun getLimit(username: String): Limitation?

    @Insert(onConflict = REPLACE)
    fun insertlimit(limit: Limitation)

    @Update(onConflict = REPLACE)
    fun updateLimit(limit: Limitation)

    @Delete
    fun deleteLimit(limit:Limitation)

}//interface