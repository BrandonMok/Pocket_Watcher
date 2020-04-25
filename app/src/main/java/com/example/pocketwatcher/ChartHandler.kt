package com.example.pocketwatcher

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketwatcher.entities.Expense
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.set


class ChartHandler: AppCompatActivity() {
    /**
     * CHART STUFF
     * Class to handle the setup of piecharts
     * Styling && data setup
     */

    /**
     * setupPieChart
     * Sets up styling and setting of chart settings
     */
    fun setupPieChart(v: View){
        var chart = v.findViewById<PieChart>(R.id.piechart)
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.transparentCircleRadius = 60f
        chart.animateY(1000, Easing. EaseInOutCubic)
        chart.legend.isEnabled = false
        chart.setNoDataText("No logged expenses!")
        chart.setNoDataTextColor(Color.BLACK)
    }

    /**
     * setupPieChartData
     * Sets up the actual data of the chart
     */
    fun setupPieChartData(v: View, activity: Activity, expList: MutableList<Expense>?) {
        var chart = v.findViewById<PieChart>(R.id.piechart)

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
        else {
            //make chart gone so it's invisible and not taking space
            chart.visibility = View.GONE
        }


        //List of colors for chart to use
        var colors = ArrayList<Int>()
        colors.add(activity.resources.getColor(R.color.blue))
        colors.add(activity.resources.getColor(R.color.green))
        colors.add(activity.resources.getColor(R.color.yellow))
        colors.add(activity.resources.getColor(R.color.red))
        colors.add(activity.resources.getColor(R.color.darkBlue))
        colors.add(activity.resources.getColor(R.color.lightPurple))
        colors.add(activity.resources.getColor(R.color.pink))

        //Convert list of PieEntries to PieDataSet
        var dataSet = PieDataSet(pieEntryList, "Expenses")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = colors


        //Convert PieDataset to PieData
        var data = PieData(dataSet)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(20f)
        chart.data = data
        chart.invalidate() // refresh
    }//setupPieChartData
} //class