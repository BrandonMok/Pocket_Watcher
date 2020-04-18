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
    fun insertExpense(expense: Expense)

    @Update(onConflict = REPLACE)
    fun updateExpense(expense: Expense)

    @Delete
    fun deleteExpense(expense: Expense)

}//interface