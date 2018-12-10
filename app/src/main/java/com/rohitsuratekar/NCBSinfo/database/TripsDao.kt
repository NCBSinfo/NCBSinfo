package com.rohitsuratekar.NCBSinfo.database

import androidx.room.*

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Dao
interface TripsDao {

    @Insert
    fun insertTrips(tripData: TripData): Long

    // Will return 0 id not found
    @Query(
        "SELECT tripID FROM trips " +
                "WHERE routeID LIKE :routeID AND " +
                "day LIKE :day "
    )
    fun getTripID(routeID: Int, day: Int): Int

    @Update
    fun updateTrips(tripData: TripData)

    @Query("SELECT * FROM trips " + "WHERE routeID LIKE :routeID ")
    fun getTripsByRoute(routeID: Int): List<TripData>

    @Query("SELECT * FROM trips WHERE tripID LIKE :tripID")
    fun getTrip(tripID: Int): TripData

    @Delete
    fun deleteRoute(tripData: TripData)

    @Query("DELETE FROM trips " + "WHERE routeID LIKE :routeID ")
    fun deleteTripsByRoute(routeID: Int)

    @Query("DELETE FROM trips " + "WHERE tripID LIKE :tripID ")
    fun deleteTripsByTrip(tripID: Int)

    @Query("DELETE FROM trips")
    fun deleteAll()
}
