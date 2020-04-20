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

        if(savedInstanceState != null){
            // If no fragment in layout, add OverviewFragment() as initial fragment
            if(supportFragmentManager.findFragmentById(R.id.frame_layout) == null){
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout, overviewFragment)
                    .commit()
            }//endif
        }
        else {
            // If no fragment in layout, add OverviewFragment() as initial fragment
            if (supportFragmentManager.findFragmentById(R.id.frame_layout) == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout, OverviewFragment())
                    .commit()
            }//endif
        }

        menuSetup()
    }//oncreate


    /**
     * onSaveInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

//        supportFragmentManager.putFragment(outState, "overview", overviewFragment!!)
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

                }
                R.id.monthly_expenses -> {

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
