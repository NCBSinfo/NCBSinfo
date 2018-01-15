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
public interface RouteDao {

    @Query("SELECT * FROM routes")
    List<RouteData> getRouteNames();

    @Query("SELECT * FROM routes WHERE routeID = :routeID")
    RouteData getRoute(int routeID);

    @Insert
    long insertRoute(RouteData routeData);

    @Update
    void updateRoute(RouteData routeData);

    @Delete
    void deleteRoute(RouteData routeData);

    @Query("DELETE FROM routes")
    void deleteAll();

    // Will return 0 id not found
    @Query("SELECT routeID FROM routes " +
            "WHERE origin LIKE :origin AND " +
            "destination LIKE :destination AND " +
            "type LIKE :type")
    int getRouteNo(String origin, String destination, String type);

    @Query("UPDATE routes SET favorite = 'no'")
    void removeAllFavorite();

    @Query("UPDATE routes SET favorite = 'yes' WHERE routeID = :routeID")
    void setFavorite(int routeID);

    @Query("UPDATE routes SET synced = :timestamp")
    void updateSync(String timestamp);

    @Query("DELETE FROM routes " +
            "WHERE routeID LIKE :routeID "
    )
    void deleteRouteByNumber(int routeID);
}