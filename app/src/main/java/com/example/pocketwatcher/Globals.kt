package com.example.pocketwatcher

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

<<<<<<< HEAD

/**
 * Globals
 * Class to hold global reusble methods
 */
class Globals : AppCompatActivity() {
=======
/**
 * Globals class to hold global reusable functions
 */
class Globals: AppCompatActivity() {
>>>>>>> dev

    /**
     * changeFragment
     */
<<<<<<< HEAD
    fun changeFragment(v: View, context: Context, fragment: Fragment) {
        var activity = context as AppCompatActivity

        var ft = activity?.supportFragmentManager?.beginTransaction()
        ft!!.replace(R.id.frame_layout, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }//changeFragment


=======
    fun changeFragment(v: View, context: Context, fragment: Fragment){
        var activity = context as AppCompatActivity

        var ft = activity?.supportFragmentManager?.beginTransaction()
            ft!!.replace(R.id.frame_layout, fragment)
            ft.addToBackStack(null)
            ft.commit()
    }//changeFragment

>>>>>>> dev
}//class