package com.example.pocketwatcher

import androidx.room.*
import android.content.Context
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.dao.ExpenseDao

@Database(entities = arrayOf(Expense::class), version = 1, exportSchema = false)
abstract class PocketWatcherDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        //the only instance singleton
        private var INSTANCE: PocketWatcherDatabase? = null

        //gets the singleton
        @Synchronized
        fun getInstance(context: Context): PocketWatcherDatabase {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    PocketWatcherDatabase::class.java,
                    "expense.db").fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }//get instance
    }//companion obj
}//database