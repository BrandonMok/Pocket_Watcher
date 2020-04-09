package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketwatcher.entities.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_overview.*

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }//onCreateView

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var pref = activity!!.getSharedPreferences("USERS",0)
        var user = pref.getString("CURRENT_USER", "")

        if(user.equals("") || user == null){
            // user's username wasn't set on login - redirect back to login
            startActivity(Intent(activity, MainActivity::class.java))
        }

        var userObj = Gson().fromJson(user, User::class.java)

        // Set custom text
        helloTextView.text = "Hello ${userObj.username}!"
    }//onViewCreated

}//fragment
