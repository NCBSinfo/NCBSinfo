package com.rohitsuratekar.NCBSinfo.di

import android.app.Application
import androidx.room.Room
import com.rohitsuratekar.NCBSinfo.database.LocalData
import com.rohitsuratekar.NCBSinfo.database.RouteDao
import com.rohitsuratekar.NCBSinfo.database.TripsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(application: Application) {

    private val db: LocalData =
        Room.databaseBuilder(application, LocalData::class.java, "NCBSinfo")
            .fallbackToDestructiveMigration() //Need this to remove all old databases
            .build()

    @Singleton
    @Provides
    fun providesLocalDatabase(): LocalData {
        return db
    }

    @Singleton
    @Provides
    fun providesRouteDao(database: LocalData): RouteDao {
        return database.routeDao
    }

    @Singleton
    @Provides
    fun providesTripsDao(database: LocalData): TripsDao {
        return database.tripDao
    }


}