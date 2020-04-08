package com.rohitsuratekar.NCBSinfo.models

import com.rohitsuratekar.NCBSinfo.common.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal class TripDay @Throws(ParseException::class)
constructor(rawTrips: List<String>) {
    private val today = ArrayList<String>()
    private val tomorrow = ArrayList<String>()

    init {

        val format = SimpleDateFormat(Constants.FORMAT_TRIP_LIST, Locale.ENGLISH)
        //TODO
        if (rawTrips.isNotEmpty()) {

            val firstTrip = format.parse(rawTrips[0])!!
            today.add(rawTrips[0]) //Add first trip.
            //Now if date is before first date, it should be after midnight trip
            for (trip in rawTrips) {
                if (firstTrip.before(format.parse(trip))) {
                    today.add(trip)
                } else if (firstTrip.after(format.parse(trip))) {
                    tomorrow.add(trip)
                }
            }
        }
    }

    fun getToday(): List<String> {
        return today
    }

    fun getTomorrow(): List<String> {
        return tomorrow
    }
}
