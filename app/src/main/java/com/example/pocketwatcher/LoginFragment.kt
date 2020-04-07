package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_login.*

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
        var password = loginSignUp.hashPassword(passwordTextView.text.toString())

        // CHECK: That both inputs have values
//        if(!username.equals("") && username != null && !password.equals("") && password != null){
//            var sp = getSharedPreferences(username,0)       // get sharedPreferences for user
//
//            // CHECK: if 'Status' key is true, then it was set on registrations
//            if(sp.contains("Status")){
//                var spPwd = sp.getString("Password","") //pwd in user preferences
//
//                // CHECK: hashed passwords match for this user
//                if(password.equals(spPwd)){
//                    /**
//                     * Keep track of current user's username
//                     */
//                    var generalSP = getSharedPreferences("CURRENT_USER", 0)
//                    var editor = generalSP.edit()
//                    editor.putString("USERNAME", username)
//                    editor.commit()
//
//
//                    // Good to go -> redirect to overview
//                    var overviewIntent = Intent(this,OverviewActivity::class.java)
//                    overviewIntent.putExtra("USER_NAME", username)
//                    startActivity(overviewIntent)
//                    finish()
//                }
//                else {
//                    // INCORRECT PWD
//                    loginSignUp.makeToast("Incorrect username or password!", this).show()
//                }
//            }
//            else {
//                // User doesn't exist
//                loginSignUp.makeToast("Account doesn't exist!", this).show()
//            }
//        }
//        else {
//            // Both inputs weren't entered
//            loginSignUp.makeToast("Please enter username and password!", this).show()
//        }
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
