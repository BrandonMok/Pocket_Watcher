package com.example.pocketwatcher.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import com.example.pocketwatcher.PocketWatcherDatabase
import com.example.pocketwatcher.entities.Expense
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ExpenseViewModel (application: Application) : AndroidViewModel(application){
    private var allExpenses: MutableList<Expense> = mutableListOf()
    var mAllExpenses: MutableLiveData<MutableList<Expense>> = MutableLiveData()

    // Database
    private val database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(this.getApplication())

    init {
        doAsync {
            //allExpenses = database.taskDao().getAllTasks().toMutableList()
           // mAllExpenses.postValue(allTasks)
        }
    }//init



}//class

