package com.example.pocketwatcher

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.math.exp

class ExpenseListAdapter (private var expenseList: MutableList<Expense>,
                          private var context: Context,
                          private var expenseListViewModel: ExpenseListViewModel,
                          private var fragmentManager: FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * onCreateViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_expenseitem, parent,false))
    }

    /**
     * onBindViewHolder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val expense = expenseList[position]
        val vh = holder as ExpenseListAdapter.RecyclerViewHolder
        vh.title!!.text = expense.title
        vh.value!!.text = "$" + expense.value.toString()
        vh.date!!.text = expense.date
        vh.tag!!.text = expense.tag

        //onclick listener for the clicked recyclerview item
        vh.itemView.setOnClickListener{
            val expense = expenseList[position]
            EditDeleteExpenseDialogFragment(expenseListViewModel, expense).show(fragmentManager, "Edit")
        }
    }//onBindViewHolder

    /**
     * getItemCount
     * RecyclerAdapter function
     */
    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun getExpenseList(): MutableList<Expense> {
        return this.expenseList
    }

    /**
     * addExpense
     */
    fun addExpenses(expenseList: MutableList<Expense>){
        this.expenseList = expenseList
        notifyDataSetChanged()
    }

    /**
     * addExpenses
     */
    fun addExpense(expense: Expense){
        doAsync {
            var db = PocketWatcherDatabase.getInstance(context)
            var exp = db.expenseDao().getExpenseById(expense.id)

            uiThread {
                if(exp == null){
                    //dne good to add
                    doAsync {
                        db.expenseDao().insertExpense(expense)

                        uiThread {
                            expenseList.add(expense)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }



    /**
     * RecyclerViewHolder class
     */
    internal class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var value: TextView
        var tag: TextView?
        var date: TextView

        init {
            title = view.findViewById(R.id.titleTextView)
            value = view.findViewById(R.id.valueTextView)
            tag = view.findViewById(R.id.tagValueTextView)
            date = view.findViewById(R.id.dateTextView)
        }//init
    }//viewholder
}//class