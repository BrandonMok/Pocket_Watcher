package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.google.gson.Gson
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class WeeklyExpenseFragment : Fragment() {

    lateinit var mAdapter: ExpenseListAdapter                       //Adapter
    private lateinit var recyclerView: RecyclerView                 //RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager  //LayoutManager
    private lateinit var expenseListViewModel: ExpenseListViewModel //ExpenseListViewModel
    private var globals = Globals()
    private var chartHandler = ChartHandler()
    private var localList: MutableList<Expense>? = null
    private var total: Double = 0.0

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currUser = globals.getCurrentUser(activity!!, Gson())
        var currUsername = currUser!!.username

        var tpMap = HashMap<String, String>()
        tpMap.put("Period", "Weekly")               //TimePeriod -> Weekly
        tpMap.put("Date", TimePeriod().getWeek())   //Get && pass the dateString for this week (i.e. 2020-04-19:2020-04-25)
        expenseListViewModel = ExpenseListViewModel(activity?.application!!, currUsername, tpMap)
        mAdapter = ExpenseListAdapter(mutableListOf(), context!!, expenseListViewModel, activity!!.supportFragmentManager)

        //Observer - viewmodel to add expenses to adapter if change on viewmodel's data
        expenseListViewModel.mAllExpenses.observe(this,
            Observer<MutableList<Expense>> {expense ->
                mAdapter.addExpenses(expense!!)

                localList = expense!!
                chartHandler.setupPieChartData(view!!, activity!!, localList)//Have the created ChartHandler class setup Piechart's data
                calcTotal(localList)
            })

        //LIMIT
        var limitObj = globals.getLimitFromSharedPref(activity!!, Gson())
        if(limitObj != null){
            var noLimitFragment = NoLimitFragment()
            var args = Bundle()
            args.putString(NoLimitFragment.ARG_LIMIT_USED, total.toString())
            args.putString(NoLimitFragment.ARG_LIMIT, limitObj.weekly)
            noLimitFragment.arguments = args

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.limitFrameLayout, noLimitFragment)
                .commit()
        }//endif limit
    }//onCreate

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weekly_expense, container, false)  //fragment layout
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView        // find recyclerView
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter                                              // set recyclerView's adapter
        return view
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartHandler.setupPieChart(view)  //Have the created ChartHandler class setup Piechart's styling

        //Add touch listener to recyclerview
        globals.setRecyclerViewItemTouchListener(view, mAdapter, recyclerView, expenseListViewModel)
    }//onViewCreated


    /**
     * companion object
     */
    companion object {
        @JvmStatic
        fun newInstance() =
            WeeklyExpenseFragment().apply {

            }
    }

    /**
     * calcTotal
     */
    private fun calcTotal(expList: MutableList<Expense>?){
        if(expList != null && expList.size != 0) {
            for(exp in expList){
                total = total?.plus(exp.value)
            }
        }
    }
}//fragment
