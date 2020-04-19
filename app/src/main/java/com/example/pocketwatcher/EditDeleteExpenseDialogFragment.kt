package com.example.pocketwatcher

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class EditDeleteExpenseDialogFragment(expenseListViewModel: ExpenseListViewModel) : DialogFragment() {
    //Global
    private var globals = Globals()
    private var titleET: EditText? = null
    private var valueET: EditText? = null
    private var tagET: EditText? = null

    var expenseListViewModel = expenseListViewModel

    /**
     * onCreateDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity!!.layoutInflater
        var view: View = inflater.inflate(R.layout.fragment_add_expense_dialog, null)

        builder.setView(view)
            .setTitle("Edit Expense")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int -> })
            .setPositiveButton("Edit", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                if(!titleET!!.text.equals("") && titleET != null &&
                    !valueET!!.text.equals("") && valueET != null){

                    if(Pattern.compile( "[0-9]" ).matcher( valueET!!.text.toString() ).find()){

                        /**
                         * TODO to update
                         * Get it first?
                         */


                        // Expense object
                        var expense = Expense(
                            titleET!!.text.toString(),
                            valueET!!.text.toString().toDouble(),
                            tagET!!.text.toString(),
                            TimePeriod().getToday(),
                            globals.getCurrentUser(activity!!, Gson())!!.username
                        )

                        doAsync {
                            expenseListViewModel.updateExpense(expense)
                        }
                    }
                }
                else {
                    // ISSUE:
                    // Doesn't show for some reason
                    globals.makeAlertDialog(activity!!, "Invalid Values", "Please try again!")
                }
            })

        titleET = view.findViewById(R.id.titleEditText)
        valueET = view.findViewById(R.id.valueEditText)
        tagET = view.findViewById(R.id.tagEditText)
        return builder.create()
    }


    /**
     * deleteExpense
     */
    fun deleteExpense(){
        /**
         * TODO
         */
    }
}//fragment
