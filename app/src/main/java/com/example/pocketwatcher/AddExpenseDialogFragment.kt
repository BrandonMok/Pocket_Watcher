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
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.pocketwatcher.entities.Expense
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_expense_dialog.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 */
class AddExpenseDialogFragment : DialogFragment() {
    //Global
    private var globals = Globals()

    private var titleET: EditText? = null
    private var valueET: EditText? = null
    private var tagET: EditText? = null



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
            .setPositiveButton("Add", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                if(!titleET!!.text.equals("") && titleET != null &&
                    !valueET!!.text.equals("") && valueET != null){

                    // Expense object
                    var expense = Expense(
                        titleET!!.text.toString(),
                        valueET!!.text.toString().toDouble(),
                        tagET!!.text.toString(),
                        TimePeriod().getToday(),
                        globals.getCurrentUser(activity!!, Gson())!!.username
                    )

                    doAsync {
                        PocketWatcherDatabase.getInstance(context!!).expenseDao().insertExpense(expense)

                        uiThread {
                            return@uiThread
                        }
                    }
                }
                else {
                    // ISSUE:
                    // Doesn't show
                    globals.makeToast("Invalid values. Please try again!", context!!).show()
                }
            })

        titleET = view.findViewById(R.id.titleEditText)
        valueET = view.findViewById(R.id.valueEditText)
        tagET = view.findViewById(R.id.tagEditText)
        return builder.create()
    }
}//fragment
