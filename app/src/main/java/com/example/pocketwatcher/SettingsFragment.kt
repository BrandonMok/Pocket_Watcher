package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketwatcher.entities.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_settings.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    //loginSignup
    private var lsu = LoginSignUp()
    // Globals
    private var globals = Globals()

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

        logoutButton.setOnClickListener {
            (activity as LoggedInActivity).logout(activity!!)
        }

        submitButton.setOnClickListener { submitButtonOnClick(view) }
    }

    /**
     * submitButtonOnClick
     */
    fun submitButtonOnClick(v: View){
        // CHECK: If textview isn't empty first
        var newPWD = passwordTextView.text.toString()

        if(!newPWD.equals("") && newPWD != null){
            newPWD = lsu.hashPassword(passwordTextView.text.toString())

            var gson = Gson()
            var userObj = Globals().getCurrentUser(activity!!, gson)
            userObj!!.password = newPWD

            doAsync {
                PocketWatcherDatabase.getInstance(context!!).userDao().updateUser(userObj!!)

                uiThread {
                    // Update current user in sharedpref
                    activity!!.getSharedPreferences("USERS",0)
                        .edit()
                        .putString("CURRENT_USER", gson.toJson(userObj))
                        .commit()


                    passwordTextView.text = null
                    globals.makeToast("Password Changed Successfully!", context!!).show()
                }
            }
        }
        else {
            // Not allowed password
            globals.makeToast("Please enter a valid password!", context!!).show()
        }
    }//submitButtonOnClick
}//fragment
