package com.example.pocketwatcher

import android.Manifest.permission_group.CALENDAR
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_limit.*
import java.lang.Double.parseDouble
import java.lang.NumberFormatException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LimitFragment : Fragment() {
    private var loginSignUp = LoginSignUp()

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dailyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
        weeklyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
        monthlyEditText.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })

        dailyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) { calculateLimits(s.toString(), R.id.dailyEditText) }
            override fun onTextChanged( s: CharSequence,  start: Int, before: Int, count: Int) {}
        })
        weeklyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) { calculateLimits(s.toString(), R.id.weeklyEditText) }
            override fun onTextChanged( s: CharSequence,  start: Int, before: Int, count: Int) {}
        })
        monthlyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) { calculateLimits(s.toString(), R.id.monthlyEditText) }
            override fun onTextChanged( s: CharSequence,  start: Int, before: Int, count: Int) {}
        })
    }//onViewCreated

    /**
     * calculateLimits
     */
    private fun calculateLimits(value: String, enteredEditTextID: Int){
        try {
            var numVal = parseDouble(value)         // value entered, either daily, weekly, or monthly
            var totalDaysInMonth = getTotalDaysInMonth()// get Total # of days in current month for calculations
            var totalWeeks = getTotalWeeks()    // get total # of weeks

            when (enteredEditTextID) {
                R.id.dailyEditText -> {
                    // weekly
                    var weeklyCalcLimit = numVal * 7.0 // daily * 7 days a week for week
                    weeklyEditText.setText("$" + weeklyCalcLimit.toString())

                    // month
                    var monthlyCalcLimit = totalDaysInMonth * numVal
                    monthlyEditText.setText("$" + monthlyCalcLimit.toString())
                }
                R.id.weeklyEditText -> {
                    // given weekly

                    // daily
                    var dailyCalcLimit = numVal / 7.0
                    dailyEditText.setText("$" + dailyCalcLimit.toString())

                    //monthly
                    var monthlyCalcLimit = totalDaysInMonth * dailyCalcLimit
                    monthlyEditText.setText("$" + monthlyCalcLimit.toString())
                }
                R.id.monthlyEditText -> {
                    //given monthly

                    // weekly
                    var weeklyCalcLimit =  numVal / totalWeeks
                    weeklyEditText.setText("$" + weeklyCalcLimit.toString())

                    // daily
                    var dailyCalcLimit = numVal / totalDaysInMonth
                    dailyEditText.setText("$" + dailyCalcLimit.toString())
                }
            }
        } catch (e: NumberFormatException) {
            //Globals().makeAlertDialog(context!!, "Invalid value", "Please enter a valid numerical limit value!")
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

}//fragment
