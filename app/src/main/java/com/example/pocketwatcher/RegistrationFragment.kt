package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
        // Need to createUser
        var userName = usernameTextView.text.toString()
        var pwd = passwordTextView.text.toString()

<<<<<<< HEAD
    /**
     *
     */

=======
        // Grab sharedPreferences for this username & will check if this user exists
        //var sp = getSharedPreferences(userName, 0)

//        // CHECK: If userName && password have values
//        if(userName != null && !userName.equals("")){
//            if(pwd != null && !pwd.equals("")) {
//                // CHECK: if a value was returned by looking for a specific key
//                // If there's a value -> User already exists (username taken) -> ELSE User doesn't exist so make one
//                // With an actual user, will set a Status boolean key to allow this check to work
//                if (sp.contains("Status")) {
//                    loginSignUp.makeToast("Username taken!", this).show() // user already exists
//                } else {
//                    // Create & store info for new user in SP
//
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
//                    // EXPENSES
//                    // EX:
//                    // DailyExpensesMap
//                    //  -> today's date (i.e. 2020-04-04) //
//                    //      -> HashSet of expenses
//                    // var expenseMap = HashMap<String, HashMap<String, HashSet<String>>>()
////                        expenseMap.put("DailyExpenses", HashMap<String, HashSet<String>>())
////                        expenseMap.put("WeeklyExpenses", HashMap<String, HashSet<String>>())
////                        expenseMap.put("MonthlyExpenses", HashMap<String, HashSet<String>>())
//                    // var expenseMapStr = gson.toJson(expenseMap)
//
//                    var expenseMaps = HashMap<String, HashSet<String>>()
//                    var expenseMapStr = gson.toJson(expenseMaps)
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
//
//                    // Display Toast of successfully creating account
//                    loginSignUp.makeToast("Account successfully created!", this).show()
//
//                    // Redirect back to login after done!
                      //Globals().changeFragment(v, context!!, LoginFragment())
//                    redirectToLogin(v)
//                }
//            }
//            else {
//                // Invalid Password
//                loginSignUp.makeToast("Invalid Password!", this).show()
//            }
//        }
//        else {
//            // Invalid Username
//            loginSignUp. makeToast("Invalid Username!", this).show()
//        }
    }//signUp


    /**
     * redirectToLogin
     */
    private fun redirectToLogin(v: View){
        Globals().changeFragment(v, context!!, LoginFragment())
    }
>>>>>>> dev
}//fragment
