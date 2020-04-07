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
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {
    // Create Instance of LoginSignUp -> has reusable functions that both Login and Registration use
    private var loginSignUp = LoginSignUp()

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

        // CHECK: That both inputs have values
        if(username != "" && username != null && password != "" && password != null) {
            // CHECK: if username isn't already taken by an existing user
            var account: User? = PocketWatcherDatabase.getInstance(context!!).userDao().getUserByUsername(username)

            if(account != null){
                // Account doesn't exist yet with this username - create account

                /**
                 * TODO
                 */



                redirectToLogin(v)  //redirect back to login when done!
            }
            else {
                // Account with username exists already, show toast
                loginSignUp.makeToast("Username taken! Please enter an available one", context!!).show()
            }
        }
        else {
            // Empty Fields
            loginSignUp.makeToast("Please enter both a username and password!", context!!).show()
        }


        // Grab sharedPreferences for this username & will check if this user exists
        //var sp = getSharedPreferences(userName, 0)

//                    // Limit
//                    // GSON used to convert structures/data to strings that sharedPref can accept
//                    var gson = Gson()
//
//                    // LIMIT
//                    var limitMap = HashMap<String, String>()
//                    limitMap.put("Daily", "")
//                    limitMap.put("Weekly", "")
//                    limitMap.put("Monthly", "")
//                    var limitMapStr = gson.toJson(limitMap) // convert map to string for s.p
//
//
//
//
//                    // Editor to add all keys into sharedPreferences
//                    var editor = sp.edit()
//                    editor.putBoolean("Status", true)
//                    editor.putString("Username", userName)
//                    editor.putString("Password", loginSignUp.hashPassword(pwd))
////                     for expenses, will have all expenses be based of on a date
////                     for daily (if today), weekly (within this week's range), monthly (this month) -> sorting based on all
////                     Value of key "Expenses" will be a json string (use gson to convert to obj)
//                    // editor.putString("Expenses", expenseMapStr)
//                    editor.putString("DailyExpenses", expenseMapStr)
//                    editor.putString("WeeklyExpenses", expenseMapStr)
//                    editor.putString("MonthlyExpenses", expenseMapStr)
//                    editor.putString("Limit", limitMapStr)
//                    editor.commit()
    }//signUp


    /**
     * redirectToLogin
     */
    private fun redirectToLogin(v: View){
        Globals().changeFragment(v, context!!, LoginFragment())
    }
}//fragment
