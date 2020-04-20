package com.example.pocketwatcher.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pocketwatcher.PocketWatcherDatabase
import com.example.pocketwatcher.TimePeriod
import com.example.pocketwatcher.entities.Expense
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class ExpenseListViewModel (application: Application, username: String, timeMap: HashMap<String, String>) : AndroidViewModel(application){

    //Lists
    private var allExpenses: MutableList<Expense> = mutableListOf()
    var mAllExpenses: MutableLiveData<MutableList<Expense>> = MutableLiveData()
    // Database
    private val database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(this.getApplication())
    //TimePeriod
    private var tp = TimePeriod()
    var username = username


    init {
        doAsync {
            // get ALL expenses for user
            var expenseList = database.expenseDao().getAllExpenses(username).toMutableList()

            uiThread {
                if(expenseList.isNotEmpty()){
                    var period = timeMap["Period"]      //e.g "Daily", "Weekly", "Monthly"
                    var dateStr = timeMap["Date"]       //DateString (e.g. 2020-04-4)

                    when(period){
                        "Daily" -> {
                            var today = tp.stringToDate(dateStr!!)

                            for(expense in expenseList){
                                if(tp.stringToDate(expense.date).compareTo(today) == 0){
                                    allExpenses.add(expense)
                                }
                            }
                        }
                        "Weekly" -> {
                            withinDateRange(dateStr!!, expenseList)
                        }
                        "Monthly" -> {
                            withinDateRange(dateStr!!, expenseList)
                        }
                    }

                    mAllExpenses.postValue(allExpenses.toMutableList())
                }//endif
            }//uiThread
        }//doAsync
    }//init


    /**
     * withinDateRange
     */
    private fun withinDateRange(dateString: String, expenseList: MutableList<Expense>){
        var start = tp.stringToDate(dateString.substring(0,10))
        var end = tp.stringToDate(dateString!!.substring(11,21))

        for(expense in expenseList){
            var expDate = tp.stringToDate(expense.date) //This expense's date

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
        }
        return expense
    }//getExpense


    /**
     * insertExpense
     */
    fun insertExpense(expense: Expense){
        doAsync {
            database.expenseDao().insertExpense(expense!!)
            allExpenses.add(expense!!)
            mAllExpenses.postValue(allExpenses)
        }
    }

    /**
     * updateExpense
     */
    fun updateExpense(expense: Expense){
        doAsync {
            database.expenseDao().updateExpense(expense)

            allExpenses.forEachIndexed{index, expense ->
                if(allExpenses[index].id == expense.id){
                    allExpenses[index] = expense
                }
            }
            mAllExpenses.postValue(allExpenses)
        }
    }

    /**
     * deleteExpensea
     */
    fun deleteExpense(expense: Expense){
        doAsync {
            database.expenseDao().deleteExpense(expense)
            allExpenses.remove(expense)
            mAllExpenses.postValue(allExpenses)
        }
    }

}//class

