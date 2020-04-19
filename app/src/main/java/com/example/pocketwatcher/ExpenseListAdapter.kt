package com.example.pocketwatcher

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.math.exp

class ExpenseListAdapter (private var expenseList: MutableList<Expense>,
                          private var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            //Log.d("EXPENSE", Gson().toJson(expense))

            //EditDeleteExpenseDialogFragment(expenseListViewModel).show(activity!!.supportFragmentManager, "Add")

            Snackbar.make(it, "Click detected on item ${position+1}", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
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

//            itemView.setOnClickListener {v: View ->     // could just use "it" instead of setting "v"
//                // get which item tapped on
//                val position = adapterPosition
//                //val expenseList = expenseList[position]
//
//                //EditDeleteExpenseDialogFragment(expenseListViewModel).show(activity!!.supportFragmentManager, "Add")
//
//                /**
//                 * TODO
//                 * Show alertDialog to edit + delete
//                 */
//                Snackbar.make(v, "Click detected on item ${position+1}", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .show()
//            }//onClickListener
        }//init
    }//viewholder
}//class