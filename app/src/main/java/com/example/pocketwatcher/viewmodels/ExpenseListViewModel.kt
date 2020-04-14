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

class ExpenseListViewModel (application: Application, username: String) : AndroidViewModel(application){

    private var allExpenses: MutableList<Expense> = mutableListOf()
    var mAllExpenses: MutableLiveData<MutableList<Expense>> = MutableLiveData()

    // Database
    private val database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(this.getApplication())

    init {
        doAsync {
            var expenses = database.expenseDao().getAllExpenses(username).toMutableList()

            /**
             * TODO
             * need way to have only get correct period of expenses (i.e. only daily or weekly)
             * Maybe pass another param to viewmodel??
             */
//            allExpenses =
//            mAllExpenses.postValue(allExpenses)
        }
    }//init


    /**
     * getAllExpenses
     */
    fun getAllExpenses(): MutableLiveData<MutableList<Expense>> {
        return mAllExpenses
    }


    /**
     * getExpense
     */
    fun getExpense(expID: Long): Expense? {
        var expense: Expense? = null
        doAsync {
            // do in the background
            expense = database.expenseDao().getExpenseById(expID)

            uiThread {
                return@uiThread
            }
        }
        return expense
    }//getTask


    /**
     * insertExpense
     */
    fun insertExpense(expense: Expense){
        doAsync {
            database.expenseDao().insertExpense(expense)

            // CHECK if it exists

            /**
             * TODO:
             * CHECK if expense exists,
             * use current list of expenses to find it instead of getting it all over again
             */


//            allExpenses = database.expenseDao().getAllExpenses(username).toMutableList()
//            mAllExpenses.postValue(allExpenses)
        }
    }

    /**
     * updateExpense
     */
    fun updateExpense(expense: Expense){
        doAsync {
            database.expenseDao().updateExpense(expense)

            //need to update live data. We're just going to get them all
            //probably should do a more efficient way
//            allExpenses = database.expenseDao().getAllExpenses(username).toMutableList()
//            mAllExpenses.postValue(allExpenses)
        }
    }

    /**
     * deleteExpense
     */
    fun deleteExpense(expense: Expense){
        doAsync {
            database.expenseDao().deleteExpense(expense)

            //need to update live data. We're just going to get them all
            //probably should do a more efficient way
//            allExpenses = database.expenseDao().getAllExpenses(username).toMutableList()
//            mAllExpenses.postValue(allExpenses)
        }
    }

}//class

