package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */
@Dao
public interface RouteDao {

    @Query("SELECT * FROM routes")
    List<Route> loadRoadNames();

    @Query("SELECT * FROM routes WHERE routeID = :routeID")
    Route getRoute(int routeID);

    @Insert
    void insertRoute(Route route);

    @Delete
    void deleteRoute(Route route);

    @Query("SELECT routeID FROM routes " +
            "WHERE origin LIKE :origin AND " +
            "destination LIKE :destination AND " +
            "type LIKE :type")
    int getRouteNo(String origin, String destination, String type);

}
