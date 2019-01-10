package com.rohitsuratekar.NCBSinfo.di

import com.rohitsuratekar.NCBSinfo.database.RouteDao
import com.rohitsuratekar.NCBSinfo.database.RouteData
import com.rohitsuratekar.NCBSinfo.database.TripData
import com.rohitsuratekar.NCBSinfo.database.TripsDao
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val routeDao: RouteDao,
    private val tripsDao: TripsDao
) {

    fun getAllRoutes(): List<RouteData> {
        return routeDao.routeNames
    }

    fun addRoute(routeData: RouteData): Long {
        return routeDao.insertRoute(routeData)
    }

    fun isRouteThere(origin: String, destination: String, type: String): Int {
        return routeDao.getRouteNo(origin, destination, type)
    }

    fun addTrips(tripData: TripData): Long {
        return tripsDao.insertTrips(tripData)
    }

    fun changeFavoriteRoute(routeData: RouteData) {
        routeDao.removeAllFavorite()
        routeDao.setFavorite(routeData.routeID)
    }

    fun removeFavorite() {
        routeDao.removeAllFavorite()
    }

    fun getTrips(routeData: RouteData): List<TripData> {
        return tripsDao.getTripsByRoute(routeData.routeID)
    }

    fun getRouteByNumber(routeNo: Int): RouteData {
        return routeDao.getRoute(routeNo)
    }

    fun deleteRoute(routeData: RouteData) {
        tripsDao.deleteTripsByRoute(routeData.routeID)
        routeDao.deleteRoute(routeData)
    }

    fun deleteTripsByRoute(routeNo: Int) {
        tripsDao.deleteTripsByRoute(routeNo)
    }

    fun deleteTrip(tripData: TripData) {
        tripsDao.deleteTrip(tripData)
    }


    fun deleteAll() {
        routeDao.deleteAll()
        tripsDao.deleteAll()
    }

}