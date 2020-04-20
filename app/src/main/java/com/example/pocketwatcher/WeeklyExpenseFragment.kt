package com.example.pocketwatcher

import android.graphics.Color
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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_daily_expense.*
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

    private var localList: MutableList<Expense>? = null
    private var total: Double = 0.0

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currUser = Globals().getCurrentUser(activity!!, Gson())
        var currUsername = currUser!!.username

        var tpMap = HashMap<String, String>()
        tpMap.put("Period", "Weekly")
        tpMap.put("Date", TimePeriod().getWeek())
        expenseListViewModel = ExpenseListViewModel(activity?.application!!, currUsername, tpMap)
        mAdapter = ExpenseListAdapter(mutableListOf(), context!!, expenseListViewModel, activity!!.supportFragmentManager)

        //Observer - viewmodel to add expenses to adapter if change on viewmodel's data
        expenseListViewModel.mAllExpenses.observe(this,
            Observer<MutableList<Expense>> {expense ->
                mAdapter.addExpenses(expense!!)

                localList = expense!!
                setupPieChartData(localList)
                calcTotal(localList)
            })

        //LIMIT
        var limitObj = globals.getLimitFromSharedPref(activity!!, Gson())
        if(limitObj != null){
            var noLimitFragment = NoLimitFragment()
            var args = Bundle()
            args.putString(NoLimitFragment.ARG_LIMIT_USED, total.toString())
            args.putString(NoLimitFragment.ARG_LIMIT, limitObj.daily)
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

        setupPieChart() //setup chart

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
     * setupPieChart
     * Function to setup styling of pie chart
     */
    private fun setupPieChart(){
        piechart.setUsePercentValues(true)
        piechart.description.isEnabled = false
        piechart.dragDecelerationFrictionCoef = 0.95f
        piechart.setExtraOffsets(5f, 10f, 5f, 5f)
        piechart.isDrawHoleEnabled = true
        piechart.setHoleColor(Color.WHITE)
        piechart.transparentCircleRadius = 60f
        piechart.animateY(1000, Easing. EaseInOutCubic)
        piechart.legend.isEnabled = false
        piechart.setNoDataText("No logged expenses!")
        piechart.setNoDataTextColor(Color.BLACK)
    }

    /**
     * setupPieChartData
     * converts entries from passed list to list of PieEntries that chart library understands
     */
    private fun setupPieChartData(expList: MutableList<Expense>?) {
        var pieEntryList: ArrayList<PieEntry> = ArrayList()
        var expenseMapTypes: MutableMap<String, Float>? = HashMap()

        if(expList != null && expList.size != 0){
            //Iterate through all expenses passed in to consolidate all data for piechart (e.g. "dinner", value && "dinner", value => "Dinner", value + value)
            //This way avoids having two entries for the same thing
            for(exp in expList){
                var title: String = exp.title.toUpperCase()

                if (expenseMapTypes != null) {
                    if(expenseMapTypes[title] == null){
                        //add this new Title of expense to map & it's value
                        expenseMapTypes[title] = exp.value.toFloat()
                    } else {
                        // Key already exists (e.g. "dinner" came up several times)
                        expenseMapTypes[title] = expenseMapTypes[title]!!.plus(exp.value.toFloat())
                    }
                }
            }//endfor

            //Add PieEntries into list -> this list will be used to populate the graph
            if (expenseMapTypes != null) {
                expenseMapTypes?.forEach { (key, value) ->
                    pieEntryList.add(PieEntry(value,key))
                }
            }//endif
        }

        //Colors list
        var colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.blue))
        colors.add(resources.getColor(R.color.green))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))
        colors.add(resources.getColor(R.color.darkBlue))
        colors.add(resources.getColor(R.color.lightPurple))

        //Convert list of PieEntries to PieDataSet
        var dataSet = PieDataSet(pieEntryList, "Expenses")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = colors

        //Convert PieDataset to PieData
        var data = PieData(dataSet)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(20f)
        piechart.data = data
        piechart.invalidate() // refresh
    }//setupPieChartData


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
