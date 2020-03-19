package com.rohitsuratekar.NCBSinfo.models

import android.util.Log
import com.rohitsuratekar.NCBSinfo.common.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Trip(private var tripString: String, private var dayIndex: Int, private var cal: Calendar) {

    fun displayTime(): String {
        val inputFormat = SimpleDateFormat(Constants.FORMAT_TRIP_LIST, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(Constants.FORMAT_DISPLAY_TIME, Locale.ENGLISH)
        val returnCal = Calendar.getInstance().apply { timeInMillis = cal.timeInMillis }
        val tempCal = Calendar.getInstance()
        try {
            tempCal.timeInMillis = inputFormat.parse(tripString)?.time!!
        } catch (e: ParseException) {
            Log.e("Trip", "Message : ${e.localizedMessage}")
            return "--:--"
        }
        returnCal.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY))
        returnCal.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE))
        return outputFormat.format(Date(returnCal.timeInMillis))
    }

    fun raw(): String {
        return tripString
    }

    fun tripHighlightDay(): Int {
        val tempCal = Calendar.getInstance().apply { cal.timeInMillis }
        when (dayIndex) {
            -1 -> {
                tempCal.add(Calendar.DATE, -1)
            }
            2 -> {
                tempCal.add(Calendar.DATE, 1)
            }
        }
        return tempCal.get(Calendar.DAY_OF_WEEK)
    }

    fun tripCalender(): Calendar {
        val tempCal = Calendar.getInstance().apply { cal.timeInMillis }
        when (dayIndex) {
            -1 -> {
                tempCal.add(Calendar.DATE, -1)
            }
            2 -> {
                tempCal.add(Calendar.DATE, 1)
            }
        }
        return tempCal
    }
}