package com.rohitsuratekar.NCBSinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Changed from 11 to 12 in version 62.

@Database(entities = [RouteData::class, TripData::class], version = 12, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class LocalData : RoomDatabase() {
    abstract val routeDao: RouteDao
    abstract val tripDao: TripsDao
}