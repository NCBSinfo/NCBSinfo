package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 * <p>
 * From Version 57 , all old database will be dropped and new database will be created
 */

@Database(entities = {RouteData.class, TripData.class}, version = 12)
//Changed from 11 to 12 in version 62.
public abstract class AppData extends RoomDatabase {

    private static final String DATABASE_NAME = "NCBSinfo";
    private static AppData INSTANCE;

    public abstract RouteDao routes();

    public abstract TripsDao trips();

    public static AppData getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppData.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration() //Need this to remove all old databases
                            .build();
        }
        return INSTANCE;
    }

    public static RouteDao getRoute(Context context) {
        return getDatabase(context).routes();
    }

    public static TripsDao getTrips(Context context) {
        return getDatabase(context).trips();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
