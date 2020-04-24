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
import kotlinx.android.synthetic.main.fragment_edit_delete_expense_dialog.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.NumberFormatException
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class EditDeleteExpenseDialogFragment(expenseListViewModel: ExpenseListViewModel, expense: Expense) : DialogFragment() {
    //Global
    private var globals = Globals()
    private var titleET: EditText? = null
    private var valueET: EditText? = null
    private var tagET: EditText? = null

    var expenseListViewModel = expenseListViewModel
    var expense = expense

    /**
     * onCreateDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity!!.layoutInflater
        var view: View = inflater.inflate(R.layout.fragment_add_expense_dialog, null)

        //Preset the values of the expense that user clicked && wanting to edit
        view.titleEditText.setText(expense.title)
        view.valueEditText.setText(expense.value.toString())
        view.tagEditText.setText(expense.tag)


        builder.setView(view)
            .setTitle("Edit Expense")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int -> })
            .setPositiveButton("Edit", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                if(!titleET!!.text.equals("") && titleET != null &&
                    !valueET!!.text.equals("") && valueET != null){


                    try{
                        expense.title = titleET!!.text.toString()
                        expense.value = valueET!!.text.toString().toDouble()
                        expense.tag = tagET!!.text.toString()

                        doAsync {
                            expenseListViewModel.updateExpense(expense)

                            uiThread {
                                globals.makeToast("Updated Successfully!", context!!)
                            }
                        }
                    }
                    catch(e: NumberFormatException){
                        globals.makeAlertDialog(activity!!, "Invalid Values", "Please try again!")
                    }
                }
            })

        titleET = view.findViewById(R.id.titleEditText)
        valueET = view.findViewById(R.id.valueEditText)
        tagET = view.findViewById(R.id.tagEditText)
        return builder.create()
    }
}//fragment
