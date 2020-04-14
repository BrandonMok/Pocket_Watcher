package com.example.pocketwatcher.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pocketwatcher.PocketWatcherDatabase
import com.example.pocketwatcher.entities.Expense
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class ExpenseListViewModel (application: Application, username: String, timeMap: HashMap<String, String>) : AndroidViewModel(application){

    private var allExpenses: MutableList<Expense> = mutableListOf()
    var mAllExpenses: MutableLiveData<MutableList<Expense>> = MutableLiveData()

    // Database
    private val database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(this.getApplication())

    init {
        doAsync {
            // get ALL expenses for user
            var expenseList = database.expenseDao().getAllExpenses(username).toMutableList()

            uiThread {
                // Get only add/use the expenses within specified timeperiod in timeP map
                var period = timeMap.get("Period")  //e.g "Daily", "Weekly", "Monthly"

                when(period){
                    "Daily" -> {
                        var today = Date(timeMap.get("Date"))

                        for(expense in expenseList){
                            if(Date(expense.date).compareTo(today) == 0){
                                allExpenses.add(expense)
                            }
                        }
                    }
                    "Weekly" -> {
                        withinDateRange(timeMap.get("Date")!!, expenseList)
                    }
                    "Monthly" -> {
                        withinDateRange(timeMap.get("Date")!!, expenseList)
                    }
                }
                mAllExpenses.postValue(allExpenses)
            }
        }
    }//init


    /**
     * withinDateRange
     */
    private fun withinDateRange(dateString: String, expenseList: MutableList<Expense>){
        var start = Date(dateString.substring(0,10))
        var end= Date(dateString!!.substring(11,21))

        for(expense in expenseList){
            var expDate = Date(expense.date)

            if(expDate.compareTo(start) >= 0 && expDate.compareTo(end) <= 0){
                allExpenses.add(expense)
            }
        }
    }


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
            // CHECK if it exists
            var expense = getExpense(expense.id)

            uiThread {
                if(expense == null){
                    // DNE
                    doAsync {
                        database.expenseDao().insertExpense(expense!!)

                        allExpenses.add(expense)
                        mAllExpenses.postValue(allExpenses)
                    }
                }
            }
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

