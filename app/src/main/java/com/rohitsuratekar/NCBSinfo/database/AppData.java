package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Database(entities = {RouteData.class, TripData.class}, version = 10)
//Changed from 9 to 10 in version 49
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
                            .fallbackToDestructiveMigration() //Need this to remove all old database
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

    private static final Migration MIGRATION_LATEST = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            new DatabaseMigration().migrateToLatest(db);
            Log.i(getClass().getSimpleName(), "Old data collected. Creating new data");
            List<DatabaseMigration.OldDataHolder> oldData = new DatabaseMigration().migrateToLatest(db);
            SparseArray<List<DatabaseMigration.OldDataHolder>> dataCollection = new SparseArray<>();
            //Collected all day rows
            for (DatabaseMigration.OldDataHolder holder : oldData) {
                List<DatabaseMigration.OldDataHolder> oldList = dataCollection.get(holder.getOldRouteNo(), new ArrayList<DatabaseMigration.OldDataHolder>());
                oldList.add(holder);
            }
            //Convert to Route Data and Trip Data
            for (int i = 0; i < dataCollection.size(); i++) {
                RouteData data = new RouteData();
                DatabaseMigration.OldDataHolder old = dataCollection.get(dataCollection.keyAt(i)).get(0);
                data.setOrigin(old.getOrigin());
                data.setDestination(old.getDestination());
                data.setType(old.getType());
                data.setCreatedOn(old.getCreatedOn());
                data.setModifiedOn(old.getModifiedOn());
                data.setSynced(old.getSynced());
                data.setAuthor(old.getAuthor());
                data.setFavorite(false);
                long routeID = INSTANCE.routes().insertRoute(data);
                for (DatabaseMigration.OldDataHolder oldDataHolder : dataCollection.get(dataCollection.keyAt(i))) {
                    TripData trip = new TripData();
                    trip.setDay(oldDataHolder.getDay());
                    trip.setTrips(oldDataHolder.getTripData());
                    trip.setRouteID((int) routeID);
                    INSTANCE.trips().insertTrips(trip);
                }
            }
            Log.i(getClass().getSimpleName(), "Database migrated successfully.");
        }
    };
}
