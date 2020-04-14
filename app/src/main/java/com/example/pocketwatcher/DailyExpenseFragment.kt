
package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import java.util.Observer
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.example.pocketwatcher.ExpenseListAdapter
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass.
 */
class DailyExpenseFragment : Fragment() {

    lateinit var mAdapter: ExpenseListAdapter  //Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var expenseListViewModel: ExpenseListViewModel

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currUser = Globals().getCurrentUser(activity!!, Gson())

        // Map for viewmodel to know which timeperiod and which date + how to parse it when finding expenses
        var tpMap = HashMap<String, String>()
        tpMap.put("Period", "Daily")
        tpMap.put("Date", TimePeriod().getToday())

        expenseListViewModel = ExpenseListViewModel(activity?.application!!, currUser.username, tpMap)
        mAdapter = ExpenseListAdapter(mutableListOf(), context!!)

//        expenseListViewModel.mAllExpenses.observe(this,
//            Observer<MutableList<Expense>> {expense ->
//                mAdapter.addTasks(expense!!)
//            })
    }

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_expense, container, false)  //fragment layout
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView        // find recyclerView
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter                                              // set recyclerView's adapter
        return view
    }

    /**
     * companion object
     */
    companion object {
        @JvmStatic
        fun newInstance() =
            DailyExpenseFragment().apply {

            }
    }

}//fragment
