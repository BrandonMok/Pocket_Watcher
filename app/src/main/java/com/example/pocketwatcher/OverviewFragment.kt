package com.example.pocketwatcher

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.entities.Limitation
import com.example.pocketwatcher.entities.User
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    private var db: PocketWatcherDatabase? = null   //db
    private var globals = Globals() //globals
    private var tp = TimePeriod()   //timeperiod
    private var gson = Gson()

    private var user: User? = null

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }//onCreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = PocketWatcherDatabase.getInstance(context!!)   //set instance of db

        user = globals.getCurrentUser(activity!!, gson) //get current user's object
        var username = user!!.username          // get username of current user

        if(user == null){
            // user'sl username wasn't set on login - redirect back to login
            startActivity(Intent(activity, MainActivity::class.java))
        }

        helloTextView.text = "Hello $username!" // Set custom text

        //LIMIT
        var limitSet = false
        var limitObj = globals.getLimitFromSharedPref(activity!!, gson)
        if(limitObj != null){
            limitEditText.setText(limitObj.daily)
            limitSet = true
            noLimitTextView.visibility = View.GONE
        }
        else {
            //Not in sp, so get it from DB && store it in sp to prevent further having to keep getting from db
            doAsync {
                var limitObj = db!!.limitationDao().getLimit(username)
                if(limitObj != null){
                    uiThread {
                        limitEditText.setText(limitObj.daily)
                        noLimitTextView.visibility = View.GONE
                        limitSet = true
                    }
                }
            }
        }

        //EXPENSES:
        // CHECK for expenses
        doAsync {
            var expenseList: List<Expense>? = db!!.expenseDao()!!.getAllExpenses(username)

            uiThread {
                if (expenseList != null && expenseList.isNotEmpty()) {
                    // There are expenses made from user
                    // Calculate all totals
                    var today = tp.stringToDate(tp.getToday())

                    var entireWeekString = tp.getWeek()
                    var startWeek = tp.stringToDate(entireWeekString.substring(0, 10))
                    var endWeek = tp.stringToDate(entireWeekString.substring(11, 21))

                    var entireMonthString = tp.getMonth()
                    var startMonth = tp.stringToDate(entireMonthString.substring(0, 10))
                    var endMonth = tp.stringToDate(entireMonthString.substring(11, 21))

                    var dailyTotal: Double = 0.0
                    var weeklyTotal: Double = 0.0
                    var monthlyTotal: Double = 0.0

                    for (exp in expenseList) {
                        var expDate = tp.stringToDate(exp.date)

                        if (expDate.equals(today)) {
                            // Today
                            dailyTotal += exp.value
                        }
                        if (expDate.compareTo(startWeek) >= 0 && expDate.compareTo(endWeek) <= 0) {
                            // Week
                            weeklyTotal += exp.value
                        }
                        if (expDate.compareTo(startMonth) >= 0 && expDate.compareTo(endMonth) <= 0) {
                            // month
                            monthlyTotal += exp.value
                        }
                    }

                    //If limit is set, display the total value used for the overview
                    if (limitSet) {
                        limitUsedEditText.setText(dailyTotal.toString())
                    }

                    displayExpenseValues(dailyTotal.toString(), weeklyTotal.toString(), monthlyTotal.toString())
                }
            }
        }

        // set onClickListeners
        ConstraintLayoutDE.setOnClickListener {
            globals.changeFragment(view, context!!, DailyExpenseFragment())
        }
        ConstraintLayoutWE.setOnClickListener {
            globals.changeFragment(view, context!!, WeeklyExpenseFragment())
        }
        ConstraintLayoutME.setOnClickListener {
            globals.changeFragment(view, context!!, MonthlyExpenseFragment())
        }
    }//onViewCreated


    /**
     * displayExpenseValues
     */
    private fun displayExpenseValues(dailyTotal: String, weeklyTotal: String, monthlyTotal: String){
        dailyExpenseValueTextView.setText("$" + dailyTotal)
        weeklyExpenseValueTextView.setText("$" + weeklyTotal)
        monthlyExpenseValueTextView.setText("$" + monthlyTotal)
    }

    /**
     * onSaveInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //onsaveinstancestate - add this fragment (overview) to the backstack so when relaunched and restored
        //just get from backstack and use that
        activity!!.supportFragmentManager.beginTransaction()
            .attach(this)
            .addToBackStack("overview")
            .commitAllowingStateLoss()
    }
}//fragment
