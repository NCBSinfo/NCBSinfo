package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Database(entities = {RouteData.class, TripData.class}, version = 3)
public abstract class AppData extends RoomDatabase {

    private static final String DATABASE_NAME = "NCBSinfo";
    private static AppData INSTANCE;

    public abstract RouteDao routes();

    public abstract TripsDao trips();

    public static AppData getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppData.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_LATEST)
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

    private static final Migration MIGRATION_LATEST = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            new DatabaseMigration().migrateToLatest(db);
        }
    };
}
