
package com.example.pocketwatcher

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.example.pocketwatcher.ExpenseListAdapter
import com.example.pocketwatcher.entities.Limitation
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_daily_expense.*
import kotlinx.android.synthetic.main.fragment_no_limit.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class DailyExpenseFragment : Fragment() {

    lateinit var mAdapter: ExpenseListAdapter                       //Adapter
    private lateinit var recyclerView: RecyclerView                 //RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager  //LayoutManager
    private lateinit var expenseListViewModel: ExpenseListViewModel //ExpenseListViewModel

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var database: PocketWatcherDatabase = PocketWatcherDatabase.getInstance(context!!)
        var currUser = Globals().getCurrentUser(activity!!, Gson())
        var currUsername = currUser!!.username

        //Map for viewmodel to know which timeperiod and which date + how to parse it when finding expenses
        var tpMap = HashMap<String, String>()
        tpMap.put("Period", "Daily")
        tpMap.put("Date", TimePeriod().getToday())
        expenseListViewModel = ExpenseListViewModel(activity?.application!!, currUsername, tpMap)
        mAdapter = ExpenseListAdapter(mutableListOf(), context!!)

        //Observer
        expenseListViewModel.mAllExpenses.observe(this,
            Observer<MutableList<Expense>> {expense ->
                mAdapter.addExpense(expense!!)
            })


        //LIMIT
        //Show only if there's a limit set
        doAsync {
            var limit: Limitation? = database!!.limitationDao().getLimit(currUsername)

            uiThread {
                if(limit != null){
                    //if there's a limit set by user, show reusable fragment holding 'usedLimit' & 'limit'
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.limitFrameLayout, NoLimitFragment())
                        .commit()

                    limitEditText.setText(limit.daily)  //set the limit set on daily
                }
            }
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Setup piehcart
//        setupPieChart()
//        setupPieChartData(mAdapter.allExpenseList)

        //TEST
//        var pc = view!!.findViewById<PieChart>(R.id.piechart)
//
//        pc.setUsePercentValues(true)
//        pc.description.isEnabled = false
//        pc.dragDecelerationFrictionCoef = 0.95f
//        pc.setExtraOffsets(5f, 10f, 5f, 5f)
//        pc.isDrawHoleEnabled = true
//        pc.setHoleColor(Color.WHITE)
//        pc.transparentCircleRadius = 60f
//        pc.animateY(1000, Easing. EaseInOutCubic)
//        pc.legend.isEnabled = false
//
//        var list = listOf<PieEntry>(PieEntry(5f, "Dinner"))
//
//        var dataSet = PieDataSet(list, "Expenses")
//        dataSet.sliceSpace = 3f
//        dataSet.selectionShift = 5f
//
//        //Convert PieDataset to PieData
//        var data = PieData(dataSet)
//        data.setValueTextColor(Color.BLACK)
//        data.setValueTextSize(20f)
//        pc.data = data
//        pc.invalidate() // refresh
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
    private fun setupPieChartData(expList: MutableList<Expense>) {
        var pieEntryList: ArrayList<PieEntry>? = null
        var expenseTypes: HashMap<String, Float>? = null

        if(expList != null){
            //Iterate through all expenses passed in to consolidate all data for piechart (e.g. "dinner", value && "dinner", value => "Dinner", value + value)
            //This way avoids having two entries for the same thing
            for(exp in expList){
                var title: String = exp.title.toUpperCase()

                if(expenseTypes!!.get(title) != null){
                    //add this new Title of expense to map & it's value
                    expenseTypes.put(title, exp.value.toFloat())
                }
                else {
                    // Key already exists (e.g. "dinner" came up several times)
                    var existingType = expenseTypes.get(title)
                    existingType = existingType!!.plus(exp.value.toFloat())
                }
            }

            //Add PieEntries into list -> this list will be used to populate the graph
            expenseTypes!!.forEach { (key, value) -> pieEntryList!!.add(PieEntry(value, key)) }
        }

        //Convert list of PieEntries to PieDataSet
        var dataSet = PieDataSet(pieEntryList, "Expenses")
         dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        //Convert PieDataset to PieData
        var data = PieData(dataSet)
         data.setValueTextColor(Color.BLACK)
         data.setValueTextSize(20f)
         piechart.data = data
         piechart.invalidate() // refresh
    }//setupPieChartData

}//fragment
