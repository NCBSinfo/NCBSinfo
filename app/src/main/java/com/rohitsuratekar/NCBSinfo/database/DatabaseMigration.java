package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class DatabaseMigration {

    void migrateToLatest(SupportSQLiteDatabase db) {
        List<OldDataHolder> oldData = new ArrayList<>();
        removeOld(db);
        Log.i("DatabaseMigration", "TransportTable Database migration started");
        Cursor cursor = db.query("SELECT * FROM TransportTable", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    OldDataHolder data = new OldDataHolder();
                    data.setOrigin(cursor.getString(cursor.getColumnIndex("origin")));
                    data.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
                    data.setType(cursor.getString(cursor.getColumnIndex("type")));
                    data.setCreatedOn(cursor.getString(cursor.getColumnIndex("creation")));
                    data.setModifiedOn(cursor.getString(cursor.getColumnIndex("modified")));
                    data.setSynced(cursor.getString(cursor.getColumnIndex("synced")));
                    data.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                    List<String> trips = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("trips")),
                            new TypeToken<List<String>>() {
                            }.getType());
                    data.setTripData(trips);
                    data.setOldRouteNo(cursor.getInt(cursor.getColumnIndex("route")));
                    data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                    data.setFavorite("no");
                    oldData.add(data);
                    //Do not set Favorite property
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.execSQL("DROP TABLE IF EXISTS TransportTable");

        Log.i("DatabaseMigration", "Total of " + oldData.size() + " data entries found.");

        // Create new Table
        String schema10_route = "CREATE TABLE IF NOT EXISTS `routes` (`routeID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `origin` TEXT, `destination` TEXT, `type` TEXT, `favorite` TEXT, `creation` TEXT, `modified` TEXT, `author` TEXT, `synced` TEXT)";
        String schema10_trip = "CREATE TABLE IF NOT EXISTS `trips` (`tripID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `routeID` INTEGER NOT NULL, `trips` TEXT, `day` INTEGER NOT NULL)";
        db.execSQL(schema10_route);
        db.execSQL(schema10_trip);

        // Add Old data (if any)
        SparseArray<List<OldDataHolder>> dataCollection = new SparseArray<>();
        //Create SparseArray
        for (OldDataHolder holder : oldData) {
            dataCollection.put(holder.getOldRouteNo(), new ArrayList<OldDataHolder>());
        }
        //Add content
        for (OldDataHolder holder : oldData) {
            dataCollection.get(holder.getOldRouteNo()).add(holder);
        }

        //Convert to Route Data and Trip Data
        for (int i = 0; i < dataCollection.size(); i++) {
            ContentValues contentValues = new ContentValues();
            OldDataHolder old = dataCollection.get(dataCollection.keyAt(i)).get(0);
            Log.i("DatabaseMigration", "Creating " + old.getOrigin() + " - " + old.getDestination());
            contentValues.put("origin", old.getOrigin());
            contentValues.put("destination", old.getDestination());
            contentValues.put("type", old.getType());
            contentValues.put("favorite", "no");
            contentValues.put("creation", old.getCreatedOn());
            contentValues.put("modified", old.getModifiedOn());
            contentValues.put("author", old.getAuthor());
            contentValues.put("synced", old.getSynced());

            long routeID = db.insert("routes", 0, contentValues);
            for (OldDataHolder oldDataHolder : dataCollection.get(dataCollection.keyAt(i))) {
                ContentValues cv = new ContentValues();
                cv.put("routeID", routeID);
                cv.put("day", oldDataHolder.getDay());
                cv.put("trips", new Gson().toJson(oldDataHolder.getTripData()));
                db.insert("trips", 0, cv);
            }
        }
        Log.i(getClass().getSimpleName(), "Database migrated successfully.");
    }

    private void removeOld(SupportSQLiteDatabase db) {
        //Remove support from previous databases
        Log.i("DatabaseMigration", "Removing old databases");
        db.execSQL("DROP TABLE IF EXISTS alarmTable");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS table_contacts");
        db.execSQL("DROP TABLE IF EXISTS table_research_talk");
        db.execSQL("DROP TABLE IF EXISTS table_notifications");
    }

    class OldDataHolder extends RouteData {

        private List<String> tripData;
        private int day;
        private int oldRouteNo;

        List<String> getTripData() {
            return tripData;
        }

        void setTripData(List<String> tripData) {
            this.tripData = tripData;
        }

        int getDay() {
            return day;
        }

        void setDay(int day) {
            this.day = day;
        }

        int getOldRouteNo() {
            return oldRouteNo;
        }

        void setOldRouteNo(int oldRouteNo) {
            this.oldRouteNo = oldRouteNo;
        }
    }


}
