package com.example.pocketwatcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.gson.Gson

class SplashActivity : AppCompatActivity() {

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Check if user was already logged in before,\
        //If so, redirect right away and ignore the splashscreen
        if(Globals().getCurrentUser(this, Gson()) != null){
            startActivity(Intent(this, LoggedInActivity::class.java))
        }
        else {
            Handler().postDelayed(Runnable{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }//onCreate
}//class
