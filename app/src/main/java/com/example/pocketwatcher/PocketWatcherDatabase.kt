package com.example.pocketwatcher

import androidx.room.*
import android.content.Context
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.dao.ExpenseDao
import com.example.pocketwatcher.dao.LimitationDao
import com.example.pocketwatcher.dao.UserDao
import com.example.pocketwatcher.entities.Limitation
import com.example.pocketwatcher.entities.User

@Database(entities = arrayOf(User::class, Expense::class, Limitation::class), version = 3, exportSchema = false)
abstract class PocketWatcherDatabase : RoomDatabase() {
    // DAOs
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun limitationDao(): LimitationDao

    /**
     * companion object
     */
    companion object {
        //the only instance singleton
        private var INSTANCE: PocketWatcherDatabase? = null

        //gets the singleton
        @Synchronized
        fun getInstance(context: Context): PocketWatcherDatabase {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    PocketWatcherDatabase::class.java,
                    "pocketWatcher.db").fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }//get instance
    }//companion obj
}//database