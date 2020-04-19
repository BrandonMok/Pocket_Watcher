package com.example.pocketwatcher

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_no_limit.*
import java.lang.String

/**
 * A simple [Fragment] subclass.
 */
class NoLimitFragment : Fragment() {

    companion object {
        const val ARG_LIMIT_USED = "0.00"
        const val ARG_LIMIT = "0.00"
    }

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_limit, container, false)
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        limitUsedEditText.setText(arguments!!.getString(ARG_LIMIT_USED))
        limitEditText.setText(arguments!!.getString(ARG_LIMIT))
    }
}//fragment
