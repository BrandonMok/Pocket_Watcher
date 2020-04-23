package com.example.pocketwatcher

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.entities.Limitation
import com.example.pocketwatcher.entities.User
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_daily_expense.*
import org.jetbrains.anko.doAsync

/**
 * Globals class to hold global reusable functions
 */
class Globals: AppCompatActivity() {

    /**
     * changeFragment
     */
    fun changeFragment(v: View, context: Context, fragment: Fragment){
        var activity = context as AppCompatActivity

        var ft = activity?.supportFragmentManager?.beginTransaction()
        ft!!.replace(R.id.frame_layout, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }//changeFragment

    /**
     * getCurrentUser
     */
    fun getCurrentUser(activity: Activity, gson: Gson): User? {
        var sp = activity!!.getSharedPreferences("USERS",0)
        var userString = sp.getString("CURRENT_USER",  "")
        return gson.fromJson(userString, User::class.java)
    }

    /**
     * getLimitFromSharedPref
     */
    fun getLimitFromSharedPref(activity: Activity, gson: Gson): Limitation? {
        var sp = activity!!.getSharedPreferences("USERS",0)
        var limitString = sp.getString("LIMIT", "")
        return gson.fromJson(limitString, Limitation::class.java)
    }

    /**
     * makeToast
     * @param msg
     * Reusable function to make & show a toast
     */
    fun makeToast(msg: String, context: Context): Toast {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG)
    }

    /**
     * makeAlertDialog
     */
    fun makeAlertDialog(context: Context, title: String, msg: String){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    /**
     * setRecyclerViewItemTouchListener
     */
    fun setRecyclerViewItemTouchListener(v: View, adapter: ExpenseListAdapter, recyclerView: RecyclerView, expenseListViewModel: ExpenseListViewModel){
        val itemTouchCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                //Right - DELETE
                var list = adapter.getExpenseList()
                var deleteItem = list[position]

                doAsync {
                    expenseListViewModel.deleteExpense(deleteItem)

                    //Display snackbar allowing opportunity to undo the delete of the item
                    var snackbar = Snackbar.make(v, "Undo", Snackbar.LENGTH_LONG)
                    snackbar.setAction(R.string.snack_bar_undo, {undoDelete(expenseListViewModel, deleteItem)})
                    snackbar.show()
                }
            }//swiped
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * undoDelete
     * Action of onSwiped of deleting expense
     */
    fun undoDelete(expenseListViewModel: ExpenseListViewModel, expense: Expense){
        expenseListViewModel.insertExpense(expense)
    }
}//class