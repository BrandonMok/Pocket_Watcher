package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.entities.Limitation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    // DB
    var db: PocketWatcherDatabase? = null

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }//onCreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var gson = Gson()
        var user = Globals().getCurrentUser(activity!!, gson)
        var username = user.username

        if(user == null){
            // user'sl username wasn't set on login - redirect back to login
            startActivity(Intent(activity, MainActivity::class.java))
        }

        helloTextView.text = "Hello $username!" // Set custom text


        // variable to know if limit was set -> used later whether to show dailyLimitUsed value
        var limitSet: Boolean = false

        // CHECK: if user has a limit set
        doAsync{
            db = PocketWatcherDatabase.getInstance(context!!)
            var limit: Limitation? = db!!.limitationDao().getLimit(username)

            uiThread {
                if(limit != null ){
                    // Show limit daily limit for the overview page
                    limitEditText.setText(limit.daily)
                    limitSet = true
                }
                else {
                    // No limit set, show textview
                    noLimitTextView.visibility = View.VISIBLE
                }
            }
        }


        // CHECK for expenses
        doAsync {
            var expenseList: List<Expense> = db!!.expenseDao().getAllExpenses(username)

            uiThread {
                if(expenseList != null && expenseList.isNotEmpty()){
                    // There are expenses made from user
                    // Calculate all totals
                    var tp = TimePeriod()
                    var today = Date(tp.getToday())

                    var entireWeekString = tp.getWeek()
                    var startWeek = Date(entireWeekString.substring(0,10))
                    var endWeek = Date(entireWeekString.substring(11,21))

                    var entireMonthString = tp.getMonth()
                    var startMonth = Date(entireMonthString.substring(0,10))
                    var endMonth = Date(entireMonthString.substring(11,21))

                    var dailyTotal: Double = 0.0
                    var weeklyTotal: Double = 0.0
                    var monthlyTotal: Double = 0.0

                    for(exp in expenseList){
                        var expDate = Date(exp.date)

                        if(expDate.equals(today)){
                            // Today
                            dailyTotal += exp.value
                        }
                        if(expDate.compareTo(startWeek) >= 0 && expDate.compareTo(endWeek) <= 0){
                            // Week
                            weeklyTotal += exp.value
                        }
                        if(expDate.compareTo(startMonth) >= 0 && expDate.compareTo(endMonth) <= 0){
                            // month
                            monthlyTotal += exp.value
                        }
                    }

                    // Set values on UI
                    if(limitSet){
                        limitUsedEditText.setText("$" + dailyTotal)
                    }
                    dailyExpenseValueTextView.setText("$" + dailyTotal)
                    weeklyExpenseValueTextView.setText("$" + weeklyTotal)
                    monthlyExpenseValueTextView.setText("$" + monthlyTotal)
                }
            }
        }


        // set onClickListeners
        ConstraintLayoutDE.setOnClickListener {
            Globals().changeFragment(view, context!!, DailyExpenseFragment())
        }
        /**
         * TODO
         * Set onclicklisteners for other CL
         */

    }//onViewCreated

}//fragment
