package com.example.pocketwatcher

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_no_limit.*
import java.lang.String

/**
 * A simple [Fragment] subclass.
 */
class NoLimitFragment : Fragment() {

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_limit, container, false)
    }

    companion object {
        const val ARG_LIMIT = "0.00"
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        limitEditText.setText(arguments!!.getString(ARG_LIMIT))
    }


    /**
     * setLimitUsedValue
     * Limit Used value passed in via instance of fragment
     */
    fun setLimitUsedValue(value: kotlin.String){
        var et = view!!.findViewById<EditText>(R.id.limitUsedEditText)
        et.setText(value)
    }
}//fragment
