package com.example.pocketwatcher

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.security.MessageDigest

open class LoginSignUp  {
    /**
     * ByteArray
     */
    private fun ByteArray.toHex(): String {
        return joinToString("") { "%02x".format(it) }
    }

    /**
     * hashPassword
     * Hashes a given string
     * @param pwd
     */
    fun hashPassword(pwd: String): String{
        var bytes = MessageDigest.getInstance("SHA-256").digest(pwd.toByteArray())
        return bytes.toHex()
    }


    /**
     * hideKeyboard
     * @param v
     */
     fun hideKeyboard(v: View, context: Context){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * makeToast
     * @param msg
     * Reusable function to make & show a toast
     */
    fun makeToast(msg: String, context: Context): Toast{
        return Toast.makeText(context, msg, Toast.LENGTH_LONG)
    }
}