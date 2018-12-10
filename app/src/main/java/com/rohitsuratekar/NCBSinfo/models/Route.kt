package com.rohitsuratekar.NCBSinfo.models

import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData

data class Route(
    val routeData: RouteData,
    val tripData: List<TripData>
)