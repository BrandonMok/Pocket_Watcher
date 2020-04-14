
package com.example.pocketwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketwatcher.entities.Expense
import com.example.pocketwatcher.viewmodels.ExpenseListViewModel
import com.example.pocketwatcher.ExpenseListAdapter
import com.github.mikephil.charting.animation.Easing
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_daily_expense.*
import java.util.Observer


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
//                mAdapter.addExpense(expense!!)
//            })

        //PieChart
//        piechart.setUsePercentValues(true)
//        piechart.description.isEnabled = false
//        piechart.dragDecelerationFrictionCoef = 0.95f
//        piechart.setExtraOffsets(5f, 10f, 5f, 5f)
//        piechart.isDrawHoleEnabled = true
//        piechart.setHoleColor(Color.WHITE)
//        piechart.transparentCircleRadius = 60f
//        piechart.animateY(1000, Easing. EaseInOutCubic)
//        piechart.legend.isEnabled = false

        // ADD VALUES into chart
        // list.add(PieEntry(40f, "Dinner")

        // var dataSet = PieDataSet(list, "Expenses")
        // dataSet.sliceSpace = 3f
        // dataset.selectionShift = 5f

        // var data = PieCDData(dataSet)
        // data.setValueTextColor(Color.Black)
        // data.setValueTextSize(10f)
        // piechart.data = data
        //piechart.invalidate() // refresh
        //
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
