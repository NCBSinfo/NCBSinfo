package com.rohitsuratekar.NCBSinfo.di

import android.app.Application
import com.rohitsuratekar.NCBSinfo.common.AppPrefs
import com.rohitsuratekar.NCBSinfo.database.RouteDao
import com.rohitsuratekar.NCBSinfo.database.TripsDao
import javax.inject.Inject

class Repository @Inject constructor(
    private val routeDao: RouteDao,
    private val tripsDao: TripsDao,
    private val application: Application,
    private val prefs: AppPrefs
) {

    fun prefs(): AppPrefs {
        return prefs
    }

    fun app(): Application {
        return application
    }


}