package com.example.pocketwatcher
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Expense
 * Class to represent each expense
 * @author Brandon Mok
 */
class Expense(title: String, value: Double, tag: String?) {
    // Fields that make up an expense
    var title: String
        get() = this.title
        set(v) { this.title = v }
    var value: Double
        get() = this.value
        set(v){ this.value = v }
    var tag: String?
        get() = this.tag
        set(v){ this.tag = v }
    var date: String?     // calculated @ time of creation
        get() = getFormattedDate()

    init {
        this.title = title
        this.value = value
        this.tag = tag
        this.date = getFormattedDate()
    }

    /**
     * ReformatDate
     */
    private fun getFormattedDate(): String{
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currDate = Date()
        return sdf.format(currDate)
    }//reformatDate
}//class