package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Dao
public interface RouteDao {

    @Query("SELECT * FROM routes")
    List<RouteData> getRouteNames();

    @Query("SELECT * FROM routes WHERE routeID = :routeID")
    RouteData getRoute(int routeID);

    @Insert
    void insertRoute(RouteData routeData);

    @Delete
    void deleteRoute(RouteData routeData);

    @Query("SELECT routeID FROM routes " +
            "WHERE origin LIKE :origin AND " +
            "destination LIKE :destination AND " +
            "type LIKE :type")
    int getRouteNo(String origin, String destination, String type);

}