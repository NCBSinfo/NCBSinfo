package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@android.arch.persistence.room.Database(entities = {Route.class, Trips.class}, version = 7)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME = "NCBSinfo";
    private static Database INSTANCE;

    public abstract RouteDao routes();

    public abstract TripsDao trips();

    public static Database getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_LATEST)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static final Migration MIGRATION_LATEST = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {

        }
    };
}
