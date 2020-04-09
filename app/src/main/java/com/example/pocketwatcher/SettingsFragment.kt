package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    //loginSignup
    private var lsu = LoginSignUp()

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }//onCreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // keyboard hide when touch out of focus
        passwordTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ lsu.hideKeyboard(v, context!!) }
        })

    }

    /**
     * submitButtonOnClick
     */
    fun submitButtonOnClick(v: View){
        // CHECK: If textview isn't empty first
        var newPWD = passwordTextView.text.toString()

        if(!newPWD.equals("") && newPWD != null){
            newPWD = lsu.hashPassword(passwordTextView.text.toString())

            /**
             * TODO
             * Change password of user
             */



            passwordTextView.text = null
            lsu.makeToast("Password Changed Successfully!", context!!).show()
        }
        else {
            // Not allowed password
            lsu.makeToast("Please enter a valid password!", context!!).show()
        }
    }//submitButtonOnClick


    /**
     * logoutButtonOnClick
     */
    fun logoutButtonOnClick(v: View){
        var pref = activity!!.getSharedPreferences("USERS",0)
            .edit()
            .putString("CURRENT_USER", "")
            .commit()

        startActivity(Intent(activity, MainActivity::class.java))
    }//logoutButtonOnClick

}//fragment
