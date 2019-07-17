package com.rohitsuratekar.NCBSinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Changed from 12 to 13 in version 65.

@Database(entities = [RouteData::class, TripData::class], version = 13, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class LocalData : RoomDatabase() {
    abstract val routeDao: RouteDao
    abstract val tripDao: TripsDao
}