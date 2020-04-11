package com.example.pocketwatcher

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
    private val PATTERN = "^[0-9]+$"


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





        /**
         * TODO:
         * WIll set onchangelisteners on textviews
         */
        dailyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {calculateLimits(s.toString(), R.id.dailyEditText)}
            override fun onTextChanged( s: CharSequence,  start: Int, before: Int, count: Int) {
                //calculateLimits(s.toString(), R.id.dailyEditText)
            }
        })
    }

    /**
     * calculateLimits
     */
    private fun calculateLimits(value: String, enteredEditTextID: Int){
        try{
            var numVal = parseDouble(value)

            when(enteredEditTextID){
                R.id.dailyEditText -> {
                    /**
                     * Given daily, calculate weekly & monthly
                     * Figure out how many weeks in current month
                     */
                    // weekly
                    var weeklyCalcLimit = numVal * 7.0 // daily * 7 days a week for week
                    weeklyEditText.setText("$" + weeklyCalcLimit.toString())

                    // month
                    var maximumDays = Calendar.getInstance()
                        .getActualMaximum(Calendar.DAY_OF_MONTH)
                    var monthlyCalcLimit = maximumDays * numVal
                    monthlyEditText.setText("$" +  monthlyCalcLimit.toString())
                }
                R.id.weeklyEditText -> {
                    /**
                     * TODO
                     */
                }
                R.id.monthlyEditText -> {
                    /**
                     * TODO
                     */
                }
            }
        }
        catch(e: NumberFormatException){
            // not numeric, show message!
            /**
             * TODO
             * Show alert dialog or toast!!
             */


        }
    }//calculate limits


}//fragment
