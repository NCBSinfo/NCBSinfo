package com.rohitsuratekar.NCBSinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Changed from 14 to 15 in version 74 (Oct 2020).

@Database(entities = [RouteData::class, TripData::class], version = 15, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class LocalData : RoomDatabase() {
    abstract val routeDao: RouteDao
    abstract val tripDao: TripsDao
}