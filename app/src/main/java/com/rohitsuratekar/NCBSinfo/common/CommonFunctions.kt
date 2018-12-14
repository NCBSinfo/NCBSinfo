package com.rohitsuratekar.NCBSinfo.common

import android.util.Log
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