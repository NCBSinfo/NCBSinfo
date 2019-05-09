package com.rohitsuratekar.NCBSinfo.models

import com.rohitsuratekar.NCBSinfo.database.TripData

data class DataUpdateModel(
    var origin: String, // Origin Name
    var destination: String, // Destination Name
    var type: String, // Type of transport
    val replaceAll: Boolean, // If true, deletes all old routes and used new
    val createNew: Boolean, // If true, new route will be created if it doesn't exist
    val tripList: List<TripData>, // List of trips. If size is zero, nothing is updated
    val deleteRoute: Boolean, // If true, Route with current details will be deleted
    val updateDate: String // Date of Update
)