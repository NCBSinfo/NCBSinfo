package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Dao
public interface TripsDao {

    @Insert
    void insertTrips(TripData tripData);

    // Will return 0 id not found
    @Query("SELECT tripID FROM trips " +
            "WHERE routeID LIKE :routeID AND " +
            "day LIKE :day ")
    int getTripID(int routeID, int day);

    @Update
    void updateTrips(TripData tripData);

    @Query("SELECT * FROM trips " +
            "WHERE routeID LIKE :routeID "
    )
    List<TripData> getTripsByRoute(int routeID);

    @Query("SELECT * FROM trips WHERE tripID LIKE :tripID")
    TripData getTrip(int tripID);

    @Delete
    void deleteRoute(TripData tripData);

    @Query("DELETE FROM trips " +
            "WHERE routeID LIKE :routeID "
    )
    void deleteTripsByRoute(int routeID);

    @Query("DELETE FROM trips " +
            "WHERE tripID LIKE :tripID "
    )
    void deleteTripsByTrip(int tripID);

    @Query("DELETE FROM trips")
    void deleteAll();
}
