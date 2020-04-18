package com.example.pocketwatcher

import android.Manifest.permission_group.CALENDAR
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pocketwatcher.entities.Limitation
import com.example.pocketwatcher.entities.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_limit.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Double.parseDouble
import java.lang.NumberFormatException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LimitFragment : Fragment() {
    private var loginSignUp = LoginSignUp()  //loginSignUp
    private var globals = Globals()         //Globals
    private var currentUser: User? = null

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_limit, container, false)
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        var db = PocketWatcherDatabase.getInstance(context!!)
        currentUser = globals.getCurrentUser(activity!!, Gson())    //get Current User's username

        doAsync {
            var limit = db.limitationDao().getLimit(currentUser!!.username)

            if(limit != null){
                //Limit is set so display values
                dailyEditText.setText("$" + limit.daily)
                weeklyEditText.setText("$" + limit.weekly)
                monthlyEditText.setText("$" + limit.monthly)
            }
        }


        //onFocusChangeListeners
        //When user enters value and clicks off editText, calculate values for all
        //Didn't use ontextchange as setting of one would affect trigger ontextchange of others
        dailyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){
                loginSignUp.hideKeyboard(v, context!!)
                calculateLimits(dailyEditText.text.toString(), "DAILY")
            }
            else {
                clearEditTexts()
            }
        })
        weeklyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){
                loginSignUp.hideKeyboard(v, context!!)
                calculateLimits(weeklyEditText.text.toString(), "WEEKLY")
            }
            else {
                clearEditTexts()
            }
        })
        monthlyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){
                loginSignUp.hideKeyboard(v, context!!)
                calculateLimits(monthlyEditText.text.toString(), "MONTHLY")
            }
            else {
                clearEditTexts()
            }
        })


        //SetLimit
        setBtn.setOnClickListener { limitSetOrRemove("SET") }

        //RemoveLimit
        removeBtn.setOnClickListener { limitSetOrRemove("REMOVE") }

    }//onViewCreated


    /**
     * calculateLimits
     */
    private fun calculateLimits(value: String, period: String){
        try {
            var numVal = parseDouble(value)         // value entered, either daily, weekly, or monthly
            var totalDaysInMonth = getTotalDaysInMonth()// get Total # of days in current month for calculations
            var totalWeeks = getTotalWeeks()    // get total # of weeks

            when (period) {
                "DAILY" -> {
                    //given daily - just format to have $ symbol
                    dailyEditText.setText("$" + String.format("%.2f", dailyEditText.text.toString().toDouble()))

                    // weekly
                    var weeklyCalcLimit = numVal * 7.0
                    weeklyEditText.setText("$" + String.format("%.2f", weeklyCalcLimit))

                    // month
                    var monthlyCalcLimit = totalDaysInMonth * numVal
                    monthlyEditText.setText("$" +  String.format("%.2f", monthlyCalcLimit))
                }
                "WEEKLY" -> {
                    // given weekly
                    weeklyEditText.setText("$" + String.format("%.2f", weeklyEditText.text.toString().toDouble()))

                    // daily
                    var dailyCalcLimit = numVal / 7.0
                    dailyEditText.setText("$" + String.format("%.2f", dailyCalcLimit))

                    //monthly
                    var monthlyCalcLimit = totalDaysInMonth * dailyCalcLimit
                    monthlyEditText.setText("$" + String.format("%.2f",monthlyCalcLimit))
                }
                "MONTHLY" -> {
                    //given monthly
                    monthlyEditText.setText("$" + String.format("%.2f", monthlyEditText.text.toString().toDouble()))

                    // weekly
                    var weeklyCalcLimit =  numVal / totalWeeks
                    weeklyEditText.setText("$" + String.format("%.2f",weeklyCalcLimit))

                    // daily
                    var dailyCalcLimit = numVal / totalDaysInMonth
                    dailyEditText.setText("$" + String.format("%.2f", dailyCalcLimit))
                }
            }
        } catch (e: NumberFormatException) {
            globals.makeAlertDialog(context!!, "Invalid value", "Please enter a valid numerical limit value")
        }
    }//calculate limits


    /**
     * totalDaysInMonth
     */
    private fun getTotalDaysInMonth(): Int {
       return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * getTotalWeeksInMonth
     */
    private fun getTotalWeeks(): Int {
        var cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        var start = cal.get(Calendar.WEEK_OF_YEAR)

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        var end = cal.get(Calendar.WEEK_OF_YEAR)

        return (end - start + 1)
    }


    /**
     * limitSetOrRemove
     */
    private fun limitSetOrRemove(action: String){
        var db = PocketWatcherDatabase.getInstance(context!!)

        when(action){
            "SET" -> {
                //ADD/make new LIMIT obj to room db
                var limit = Limitation(currentUser!!.username,
                                dailyEditText.text.toString().substring(1, dailyEditText.text.toString().length),
                                weeklyEditText.text.toString().substring(1, weeklyEditText.text.toString().length),
                                monthlyEditText.text.toString().substring(1, monthlyEditText.text.toString().length))

                doAsync {
                    db.limitationDao().insertlimit(limit)

                    uiThread {
                        globals.makeToast("Limit set successfully!", context!!).show()
                    }
                }
            }
            "REMOVE" -> {
                //get current limit
                doAsync {
                    var currLimit = db.limitationDao().getLimit(currentUser!!.username)
                    db.limitationDao().deleteLimit(currLimit!!)

                    uiThread {
                        globals.makeToast("Limit removed successfully!", context!!).show()
                        clearEditTexts()
                    }
                }
            }
        }
    }


    /**
     * clearEditTexts
     */
    private fun clearEditTexts(){
        dailyEditText.setText("")
        weeklyEditText.setText("")
        monthlyEditText.setText("")
    }

}//fragment
