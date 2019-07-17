package com.rohitsuratekar.NCBSinfo.common

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun convertToList(listString: String): List<String> {
    var string = listString
    val list = ArrayList<String>()
    string = string.replace("{", "").replace("}", "")
    val tempArray = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    for (aTempArray in tempArray) {
        list.add(aTempArray.trim { it <= ' ' })
    }
    return list
}


@Throws(ParseException::class)
fun timeToDate(now: Date, s: String): Date {
    val format = SimpleDateFormat(Constants.FORMAT_TRIP_LIST, Locale.ENGLISH)

    val newCal = Calendar.getInstance().apply {
        timeInMillis = now.time
    }
    val subCal = Calendar.getInstance().apply {
        timeInMillis = format.parse(s).time
    }

    newCal.set(Calendar.HOUR_OF_DAY, subCal.get(Calendar.HOUR_OF_DAY))
    newCal.set(Calendar.MINUTE, subCal.get(Calendar.MINUTE))

    return Date(newCal.timeInMillis)
}

fun test(any: Any = "pass") {
    Log.i("TAG=======", "Testing here! $any")
}


fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun convertTimeFormat(input: String): String {
    val inputFormat = SimpleDateFormat(Constants.FORMAT_TRIP_LIST, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat(Constants.FORMAT_DISPLAY_TIME, Locale.ENGLISH)
    val tempCal = Calendar.getInstance()
    try {
        tempCal.timeInMillis = inputFormat.parse(input).time
    } catch (e: ParseException) {
        Log.e("Trip", e.localizedMessage)
        return "--:--"
    }
    return outputFormat.format(Date(tempCal.timeInMillis))
}