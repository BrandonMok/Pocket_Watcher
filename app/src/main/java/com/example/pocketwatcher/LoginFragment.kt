package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketwatcher.entities.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    // Create Instance of LoginSignUp -> has reusable functions that both Login and Registration use
    private var loginSignUp = LoginSignUp()
    private var globals = Globals()

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }//onCreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        //Check if user is loggedin
        var currUser = globals.getCurrentUser(activity!!, Gson())
        if(currUser != null){
            startActivity(Intent(activity, LoggedInActivity::class.java))
        }

        loginButton.setOnClickListener { loginOnClick(v) }
        signUpTextView.setOnClickListener { signUpOnClick(v) }

        // Clicked outside of textviews
        usernameTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ globals.hideKeyboard(v, context!!) }
        })
        passwordTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ globals.hideKeyboard(v, context!!) }
        })
    }//onViewCreated

    /**
     * loginOnClick
     * Verifies username & password with that in the savedBundle
     */
    private fun loginOnClick(v: View){
        // Grab the info from textInputs!!
        var username = usernameTextView.text.toString().trim()
        var password = passwordTextView.text.toString().trim()

        // CHECK: That both inputs have values
        if(!username.equals("") && username != null && !password.equals("") && password != null) {

            password =  loginSignUp.hashPassword(password)  //Hash password after validating for input

            doAsync {
                var account: User? = PocketWatcherDatabase.getInstance(context!!).userDao().getUserByUsername(username)

                uiThread {
                    // CHECK: if account exists or not
                    if(account != null) {
                        // Login user in
                        var accUsername = account.username
                        var accPWD = account.password

                        // Verify that username and password entered is the same as those in the db
                        if(accUsername.equals(username) && accPWD.equals(password)){
                            // Keep track of current user via sharedPreferences
                            var preferences = activity!!.getSharedPreferences("USERS", 0)
                            var prefEditor = preferences.edit()
                            prefEditor.putString("CURRENT_USER", Gson().toJson(account))
                            prefEditor.commit()

                            // Change activities - one that will have a navigation!
                            startActivity(Intent(activity, LoggedInActivity::class.java))
                        }
                        else {
                            // Login failed
                            globals.makeToast("Login failed!", context!!).show()
                        }
                    }
                    else {
                        // Login failed
                        globals.makeToast("Login failed!", context!!).show()
                    }
                }//uiThread
            }//doAsync
        }
        else {
            // Empty Fields
            globals.makeToast("Please enter a username and password!", context!!).show()
        }
    }//loginOnClick


    /**
     * signUpOnClick
     * Redirects to sign up fragment
     */
    fun signUpOnClick(v: View){
        globals.changeFragment(v, context!!, RegistrationFragment())    //swap to LoginFragment
    }
}//fragment
