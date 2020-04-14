package com.example.pocketwatcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ExpenseListAdapter (private var expenseList: MutableList<Expense>,
                          private var context: Context)
                            : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        vh.value!!.text = expense.value.toString()
        vh.date!!.text = expense.date
        vh.tag!!.text = expense.tag
    }

    /**
     * getItemCount
     * RecyclerAdapter function
     */
    override fun getItemCount(): Int {
        return expenseList.size
    }

    /**
     * addExpense
     */
    fun addTasks(expenseList: MutableList<Expense>){
        this.expenseList = expenseList
        notifyDataSetChanged()
    }

    /**
     * addExpenses
     */
    fun addTask(expense: Expense){
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
        var title: TextView? = view.findViewById(R.id.titleTextView)
        var value: TextView? = view.findViewById(R.id.valueTextView)
        var tag: TextView? = view.findViewById(R.id.tagValueTextView)
        var date: TextView? = view.findViewById(R.id.dateTextView)

//        init {
//            title = view.findViewById(R.id.titleTextView)
//            value = view.findViewById(R.id.valueTextView)
//            tag = view.findViewById(R.id.tagValueTextView)
//            date = view.findViewById(R.id.dateTextView)
//        }
    }
}//class