package com.rohitsuratekar.NCBSinfo.database

import androidx.room.*

@Dao
interface RouteDao {

    @get:Query("SELECT * FROM routes")
    val routeNames: List<RouteData>

    @Query("SELECT * FROM routes WHERE routeID = :routeID")
    fun getRoute(routeID: Int): RouteData

    @Insert
    fun insertRoute(routeData: RouteData): Long

    @Update
    fun updateRoute(routeData: RouteData)

    @Delete
    fun deleteRoute(routeData: RouteData)

    @Query("DELETE FROM routes")
    fun deleteAll()

    // Will return 0 id not found
    @Query(
        "SELECT routeID FROM routes " +
                "WHERE origin LIKE :origin AND " +
                "destination LIKE :destination AND " +
                "type LIKE :type"
    )
    fun getRouteNo(origin: String, destination: String, type: String): Int

    @Query("UPDATE routes SET favorite = 'no'")
    fun removeAllFavorite()

    @Query("UPDATE routes SET favorite = 'yes' WHERE routeID = :routeID")
    fun setFavorite(routeID: Int)

    @Query("UPDATE routes SET syncedOn = :timestamp")
    fun updateSync(timestamp: String)

    @Query("DELETE FROM routes " + "WHERE routeID LIKE :routeID ")
    fun deleteRouteByNumber(routeID: Int)

    @Query("SELECT modifiedOn FROM routes WHERE modifiedOn ORDER BY modifiedOn DESC LIMIT 1")
    fun getLastModified(): String
}