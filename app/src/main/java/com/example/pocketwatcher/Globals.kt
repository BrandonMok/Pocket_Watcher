package com.example.pocketwatcher

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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
     * logout
     */
//    fun logout(context: Context){
//        var activity = context as AppCompatActivity
//
//        activity!!.getSharedPreferences("USERS", 0)
//            .edit()
//            .putString("CURRENT_USER", "")
//            .commit()
//
//        startActivity(Intent(activity, MainActivity::class.java))
//    }

}//class