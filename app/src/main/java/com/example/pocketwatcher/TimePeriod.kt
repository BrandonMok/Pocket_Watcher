package com.example.pocketwatcher

import java.text.SimpleDateFormat
import java.util.*

/**
 * TimePeriod
 * @author Brandon Mok
 * Class to hold reusable functions regarding time periods (i.e. day, week, month)
 */
class TimePeriod {
    // Calendar
    private var calendar = Calendar.getInstance()

    // Formatters
    private val formatter = SimpleDateFormat("yyyy-MM-dd")


    /**
     * getToday
     * @return String
     * Gets and returns today's date
     */
    fun getToday(): String {
        var date = calendar.time
        var formattedDate = formatter.format(date)
        return formattedDate
    }//getToday

    /**
     * getWeek
     * @return String
     * Gets and returns the current week's range
     * EX: 2020-03-29:2020-04-04
     */
    fun getWeek(): String {
        // First day of week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var firstDayWeek = formatter.format(calendar.time)

        // Last day of week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        var lastDayOfWeek = formatter.format(calendar.time)

        return "$firstDayWeek:$lastDayOfWeek"
    }//getWeek

    /**
     * getMonth
     * @return String
     * Gets and returns the current month's range
     * EX: 2020-04-01:2020-04-30
     */
    fun getMonth(): String {
        // SET Calendar to 1st day of month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        var firstDay = formatter.format(calendar.time)

        // Get total days of current month && set calendar to last day
        var totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, totalDays)
        var lastDay = formatter.format(calendar.time)

        return "$firstDay:$lastDay"
    }//getMonth


    /**
     * stringToDate
     * Converts a stringDate to Date obj
     */
    fun stringToDate(dateStr: String): Date {
        return formatter.parse(dateStr)
    }

}// class