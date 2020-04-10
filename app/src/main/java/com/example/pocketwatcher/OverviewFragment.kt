package com.example.pocketwatcher

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketwatcher.entities.Limitation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    // DB
    var db: PocketWatcherDatabase? = null

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
        var gson = Gson()
        var user = Globals().getCurrentUser(activity!!, gson)
        var username = user.username

        if(user == null){
            // user'sl username wasn't set on login - redirect back to login
            startActivity(Intent(activity, MainActivity::class.java))
        }


        // Set custom text
        helloTextView.text = "Hello $username!"


        // CHECK: if user has a limit set
        doAsync{
            db = PocketWatcherDatabase.getInstance(context!!)
            var limit: Limitation? = db!!.limitationDao().getLimit(username)

            uiThread {
                if(limit != null ){
                    // Show limit daily limit for the overview page
                    limitEditText.setText(limit.daily)
                }
                else {
                    // No limit set, show textview
                    noLimitTextView.visibility = View.VISIBLE
                }
            }
        }
    }//onViewCreated

}//fragment
