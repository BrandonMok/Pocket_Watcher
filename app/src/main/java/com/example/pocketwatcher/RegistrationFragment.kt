package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.pocketwatcher.entities.User
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {
    // Create Instance of LoginSignUp -> has reusable functions that both Login and Registration use
    private var loginSignUp = LoginSignUp()
   // var database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(context!!)

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }//oncreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        // Set onClickListeners
        signUpBtn.setOnClickListener{ signUp(v) }
        nextTimeTextView.setOnClickListener{ redirectToLogin(v) }

        // Clicked outside of textviews
        usernameTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
        passwordTextView.setOnFocusChangeListener(View.OnFocusChangeListener(){ v, hasFocus ->
            if(!hasFocus){ loginSignUp.hideKeyboard(v, context!!) }
        })
    }//onViewCreated

    /**
     * signUp
     */
    private fun signUp(v: View){
        var username = usernameTextView.text.toString()
        var password = passwordTextView.text.toString()

            doAsync{

                var db = PocketWatcherDatabase.getInstance(context!!)
                var account: User? = db.userDao().getUserByUsername(username)

                uiThread {
                    if(account == null){
                        // Account doesn't exist yet with this username - create account
                        var hashedPassword = loginSignUp.hashPassword(password)

                        doAsync {
                            db.userDao().insertUser(User(username, hashedPassword))

                            uiThread {
                                loginSignUp.makeToast("Account created successfully!", context!!).show()
                                redirectToLogin(v)  //redirect back to login when done!
                            }
                        }
                    }
                    else {
                        // Account with username exists already, show toast
                        loginSignUp.makeToast("Username taken! Please enter an available one", context!!).show()
                    }
                }
            }
        }
        else {
            // Empty Fields
            loginSignUp.makeToast("Please enter username and password!", context!!).show()
        }
    }//signUp


    /**
     * redirectToLogin
     */
    private fun redirectToLogin(v: View){
        Globals().changeFragment(v, context!!, LoginFragment())
    }
}//fragment
