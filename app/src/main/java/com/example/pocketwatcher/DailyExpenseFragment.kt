
package com.example.pocketwatcher

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.example.pocketwatcher.ExpenseListAdapter
import com.example.pocketwatcher.entities.Limitation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_daily_expense.*
import kotlinx.android.synthetic.main.fragment_no_limit.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DailyExpenseFragment : Fragment() {

    lateinit var mAdapter: ExpenseListAdapter                       //Adapter
    private lateinit var recyclerView: RecyclerView                 //RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager  //LayoutManager
    private lateinit var expenseListViewModel: ExpenseListViewModel //ExpenseListViewModel
    private var globals = Globals()
    private var chartHandler = ChartHandler()
    private var localList: MutableList<Expense>? = null
    private var total: Double = 0.0
    private var limitUsed: MutableLiveData<Double> = MutableLiveData()


    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currUser = globals.getCurrentUser(activity!!, Gson())
        var currUsername = currUser!!.username

        //Map for viewmodel to know which timeperiod and which date + how to parse it when finding expenses
        var tpMap = HashMap<String, String>()
        tpMap.put("Period", "Daily")                //TimePeriod -> Daily
        tpMap.put("Date", TimePeriod().getToday())  //Get && pass in dateString of Today
        expenseListViewModel = ExpenseListViewModel(activity?.application!!, currUsername, tpMap)
        mAdapter = ExpenseListAdapter(mutableListOf(), context!!, expenseListViewModel, activity!!.supportFragmentManager)

        //Observer - viewmodel to add expenses to adapter if change on viewmodel's data
        expenseListViewModel.mAllExpenses.observe(this,
            Observer<MutableList<Expense>> {expense ->
                mAdapter.addExpenses(expense!!)

                localList = expense!!
                chartHandler.setupPieChartData(view!!, activity!!, localList)   //Have the created ChartHandler class setup Piechart's data
                calcTotal(localList)
            })

        //LIMIT
        var limitObj = globals.getLimitFromSharedPref(activity!!, Gson())
        if(limitObj != null){
            var noLimitFragment = NoLimitFragment()
            var args = Bundle()
            args.putString(NoLimitFragment.ARG_LIMIT, limitObj.daily)
            noLimitFragment.arguments = args

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.limitFrameLayout, noLimitFragment, "limit_fragment")
                .commit()


            //Observer - calculating total of limit used for daily
            //Using a fragment on this fragment that'll display depending if limit is set (removes need for this limit view part if no limit was set)
            //Fragment would be added already to the view, so use instance of it from finding fragment by tag "limit_fragment" and use function to pass info
            limitUsed.observe(this,
                Observer<Double> {value ->
                    total += value
                    var fragment: NoLimitFragment? = activity?.supportFragmentManager!!.findFragmentByTag("limit_fragment") as NoLimitFragment
                    if(fragment != null){
                        fragment?.setLimitUsedValue(total.toString())
                    }
                })
        }
    }//onCreate

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
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set FAB's click listener
        addFab.setOnClickListener{
            AddExpenseDialogFragment(expenseListViewModel).show(activity!!.supportFragmentManager, "Add")
        }

        chartHandler.setupPieChart(view) //Have the created ChartHandler class setup Piechart's styling

        //Add touch listener to recyclerview
        globals.setRecyclerViewItemTouchListener(view, mAdapter, recyclerView, expenseListViewModel)
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

    /**
     * calcTotal
     * Same function in all periods as it updates local values, so couldn't move to one class and reuse
     */
    private fun calcTotal(expList: MutableList<Expense>?){
        if(expList != null && expList.size != 0) {
            for(exp in expList){
                limitUsed.value = exp.value
            }
            total = 0.0
        }
        else {
            limitUsed.value = 0.0
        }
    }
}//fragment
