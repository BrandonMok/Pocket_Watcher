package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.pocketwatcher.entities.User
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    // Create Instance of LoginSignUp -> has reusable functions that both Login and Registration use
    private var loginSignUp = LoginSignUp()

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
        loginButton.setOnClickListener { loginOnClick(v) }
        signUpTextView.setOnClickListener { signUpOnClick(v) }

        // Clicked outside of textviews
        usernameTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
        passwordTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
    }//onViewCreated


    /**
     * loginOnClick
     * Verifies username & password with that in the savedBundle
     */
    private fun loginOnClick(v: View){
        // Grab the info from textInputs!!
        var username = usernameTextView.text.toString()
        var password = passwordTextView.text.toString()

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
                            Globals().changeFragment(v, context!!, OverviewFragment())  // redirect
                        }
                        else {
                            // Login failed
                            loginSignUp.makeToast("Login failed!", context!!).show()
                        }
                    }
                    else {
                        // Login failed
                        loginSignUp.makeToast("Login failed!", context!!).show()
                    }
                }//uiThread
            }//doAsync
        }
        else {
            // Empty Fields
            loginSignUp.makeToast("Please enter a username and password!", context!!).show()
        }
    }//loginOnClick


    /**
     * signUpOnClick
     * Redirects to sign up fragment
     */
    fun signUpOnClick(v: View){
        // Change fragments
        Globals().changeFragment(v, context!!, RegistrationFragment())
    }

}//fragment
