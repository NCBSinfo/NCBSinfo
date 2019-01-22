package com.rohitsuratekar.NCBSinfo.models

import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import java.util.*

class Route(
    val routeData: RouteData,
    val tripData: List<TripData>,
    var isExpanded: Boolean = false,
    var isFavorite: Boolean = false
) {
    fun tripList(calendar: Calendar): List<String> {
        val next = NextTrip(tripData)
        return next.getTrips(calendar.get(Calendar.DAY_OF_WEEK))
    }

}