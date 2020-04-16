package com.example.pocketwatcher

import android.app.AlertDialog
import android.app.Dialog
import android.app.VoiceInteractor
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_add_expense_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class AddExpenseDialogFragment : DialogFragment() {

    /**
     * onCreateDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity!!.layoutInflater
        var view: View = inflater.inflate(R.layout.fragment_add_expense_dialog, null)

        builder.setView(view)
            .setTitle("Add Expense")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int -> })
            .setPositiveButton("Ok", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                /**
                 * TODO
                 * Create Expense
                 */

            })
        return builder.create()
    }
}//fragment
