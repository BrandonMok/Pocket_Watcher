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
            // Add LoginFragment() to frame layout
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, LoginFragment())
                .commit()
        }//endif
    }//onCreate
}//activity
