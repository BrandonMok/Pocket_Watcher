package com.example.pocketwatcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 * @author Brandon Mok
 */
class MainActivity : AppCompatActivity() {

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // CHECK: If no fragment, then display splash first
        if(supportFragmentManager.findFragmentById(R.id.frame_layout) == null){
            var sfm = supportFragmentManager

            // Show Splash Fragment
            sfm.beginTransaction()
                .add(R.id.frame_layout, SplashFragment())
                .commit()

            // Then direct to login fragment
            Handler().postDelayed(Runnable{
                sfm.beginTransaction()
                    .replace(R.id.frame_layout, LoginFragment())
                    .commit()
            }, 3000)
        }//endif
    }//onCreate
}//activity
