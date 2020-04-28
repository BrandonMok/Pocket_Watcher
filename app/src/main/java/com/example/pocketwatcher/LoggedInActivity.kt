package com.example.pocketwatcher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_logged_in.*

/**
 * LoggedInActivity
 * Activity that will show all other fragment views AFTER logged in
 * Contains navigation and frame_layout to swap fragments
 */
class LoggedInActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private var globals = Globals()
    private var overviewFragment: Fragment = Fragment()

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        //Adding fragment to the frame_layout
        if(supportFragmentManager.findFragmentById(R.id.frame_layout) == null) {
            var ft = supportFragmentManager.beginTransaction()

            if(savedInstanceState != null){
                //if savedinstancestate, use the fragment's state that was saved to backstack
                ft.add(R.id.frame_layout, overviewFragment)
            }
            else {
                ft.add(R.id.frame_layout, OverviewFragment())
            }
            ft.commit()
        }

        menuSetup() // menu setup
    }//oncreate


    /**
     * onSaveInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        // Get fragment from overview fragment on saveInstanceState to display that fragment and retaining its state
        overviewFragment = supportFragmentManager.getFragment(outState, "overview")!!
    }

    /**
     * menuHandler
     * Function to setup the menu and its pathing
     */
    private fun menuSetup(){
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // responding to menu item clicks
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.overview -> {
                    navClickHandler(OverviewFragment())
                }
                R.id.daily_expenses -> {
                    navClickHandler(DailyExpenseFragment())
                }
                R.id.weekly_expenses -> {
                    navClickHandler(WeeklyExpenseFragment())
                }
                R.id.monthly_expenses -> {
                    navClickHandler(MonthlyExpenseFragment())
                }
                R.id.limitation -> {
                    navClickHandler(LimitFragment())
                }
                R.id.settings -> {
                    navClickHandler(SettingsFragment())
                }
                R.id.logout -> {
                    logout(this@LoggedInActivity)
                }
            }//when
            true
        }//listener
    }

    /**
     * onOptionsItemSelected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * navClickHandler
     */
    private fun navClickHandler(fragment: Fragment){
        globals.changeFragment(findViewById(R.id.frame_layout), this@LoggedInActivity, fragment)
        drawerLayout.closeDrawers()
    }

    /**
     * logout
     * @param context
     * Redirect to MainActivity to logout && clear sharedPreferences
     */
    fun logout(context: Context){
        var activity = context as AppCompatActivity
        activity!!.getSharedPreferences("USERS", 0)
            .edit()
            .clear()
            .commit()
        startActivity(Intent(activity, MainActivity::class.java))
        finish()
    }

}//class
