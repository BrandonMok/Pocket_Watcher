package com.example.pocketwatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_logged_in.*

class LoggedInActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        if(supportFragmentManager.findFragmentById(R.id.frame_layout) == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, OverviewFragment())
                .commit()
        }//endif

        menuHandler()
    }//oncreate


    /**
     * menuHandler
     * Function to setup the menu and its pathing
     */
    fun menuHandler(){
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // responding to menu item clicks
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.overview -> {
                    //drawerLayout.closeDrawers()
                }
                R.id.daily_expenses -> {

                }
                R.id.weekly_expenses -> {

                }
                R.id.monthly_expenses -> {

                }
                R.id.settings  -> {

                }
                R.id.logout -> {

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

}//class
