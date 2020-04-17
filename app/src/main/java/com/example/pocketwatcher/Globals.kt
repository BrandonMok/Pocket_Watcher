package com.example.pocketwatcher

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pocketwatcher.entities.User
import com.google.gson.Gson

/**
 * Globals class to hold global reusable functions
 */
class Globals: AppCompatActivity() {

    /**
     * changeFragment
     */
    fun changeFragment(v: View, context: Context, fragment: Fragment){
        var activity = context as AppCompatActivity

        var ft = activity?.supportFragmentManager?.beginTransaction()
        ft!!.replace(R.id.frame_layout, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }//changeFragment

    /**
     * getCurrentUser
     */
    fun getCurrentUser(activity: Activity, gson: Gson): User? {
        var sp = activity!!.getSharedPreferences("USERS",0)
        var userString = sp.getString("CURRENT_USER",  "")
        return gson.fromJson(userString, User::class.java)
    }

    /**
     * makeToast
     * @param msg
     * Reusable function to make & show a toast
     */
    fun makeToast(msg: String, context: Context): Toast {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG)
    }

    /**
     * makeAlertDialog
     */
    fun makeAlertDialog(context: Context, title: String, msg: String){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    /**
     * storeTotals
     */
    fun storeTotals(sp: SharedPreferences, dailyTotal: Double, weeklyTotal: Double, monthlyTotal: Double){
        var editor = sp.edit()
        editor.putBoolean("TOTALS", true)
        editor.putString("dailyTotal", dailyTotal.toString())
        editor.putString("weeklyTotal", weeklyTotal.toString())
        editor.putString("monthlyTotal", monthlyTotal.toString())
        editor.commit()
    }

    /**
     * clearTotals
     */
    fun clearTotals(sp: SharedPreferences){
        var editor = sp.edit()
        editor.remove("TOTALS")
        editor.remove("dailyTotal")
        editor.remove("weeklyTotal")
        editor.remove("monthlyTotal")
        editor.commit()
    }
}//class