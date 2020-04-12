package com.example.pocketwatcher.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.pocketwatcher.entities.Expense

@Dao
interface ExpenseDao {

    @Query("select * from expense where user = :username")
    fun getAllExpenses(username: String): List<Expense>

    @Query("select * from expense where id = :id")
    fun getExpenseById(id: Long): Expense

    @Insert(onConflict = REPLACE)
    fun insertTask(expense: Expense)

    @Update(onConflict = REPLACE)
    fun updateTask(expense: Expense)

    @Delete
    fun deleteTask(expense: Expense)

}//interface