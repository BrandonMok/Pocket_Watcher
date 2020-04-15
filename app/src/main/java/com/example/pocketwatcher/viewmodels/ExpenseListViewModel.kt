package com.example.pocketwatcher.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pocketwatcher.PocketWatcherDatabase
import com.example.pocketwatcher.TimePeriod
import com.example.pocketwatcher.entities.Expense
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


    init {
        doAsync {
            // get ALL expenses for user
            var expenseList = database.expenseDao().getAllExpenses(username).toMutableList()

            uiThread {
                if(expenseList.isNotEmpty()){
                    var period = timeMap.get("Period")  //e.g "Daily", "Weekly", "Monthly"
                    var dateStr = timeMap["Date"]       //DateString (e.g. 2020-04-4)

                    when(period){
                        "Daily" -> {
                            var today = tp.stringToDate(dateStr!!)
                            // var today = Date(dateStr!!.substring(0,4).toInt(), dateStr!!.substring(5,7).toInt(), dateStr!!.substring(8,10).toInt())

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

                    mAllExpenses.postValue(allExpenses)
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

            uiThread {
                return@uiThread
            }
        }
        return expense
    }//getExpense


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
            var expenseToUpdate = getExpense(expense.id)

            uiThread {
                if(expenseToUpdate != null){
                    // Exists!
                    doAsync {
                        database.expenseDao().updateExpense(expense)

                        uiThread {
                            // Replace that expense in the list
                            for(exp in allExpenses.indices){
                                if(allExpenses[exp].id == expense.id){
                                    allExpenses[exp] = expense
                                }
                            }
                            mAllExpenses.postValue(allExpenses)
                        }
                    }
                }
            }
        }
    }

    /**
     * deleteExpense
     */
    fun deleteExpense(expense: Expense){
        doAsync {
            var expenseToDelete = getExpense(expense.id)

            uiThread {
                if(expenseToDelete != null){
                    doAsync {
                        database.expenseDao().deleteExpense(expense)

                        uiThread {
                            allExpenses.remove(expense)
                            mAllExpenses.postValue(allExpenses)
                        }
                    }
                }
            }
        }
    }

}//class

