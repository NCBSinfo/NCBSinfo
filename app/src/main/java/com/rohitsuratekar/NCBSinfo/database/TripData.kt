package com.rohitsuratekar.NCBSinfo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.rohitsuratekar.NCBSinfo.database.TripData.Companion.TRIPS_TABLE

@Entity(tableName = TRIPS_TABLE)
@TypeConverters(ListConverter::class)
class TripData {

    @PrimaryKey(autoGenerate = true)
    @Expose
    var tripID: Int = 0
    @Expose
    var routeID: Int = 0
    @Expose
    var trips: List<String>? = null
    @Expose
    var day: Int = 0

    companion object {
        const val TRIPS_TABLE = "trips"
    }
}
