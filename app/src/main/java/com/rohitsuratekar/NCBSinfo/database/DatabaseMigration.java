package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
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

    List<OldDataHolder> migrateToLatest(SupportSQLiteDatabase db) {
        List<OldDataHolder> oldData = new ArrayList<>();
        removeOld(db);
        Log.i(getClass().getName(), "TransportTable Database migration started");
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
                    data.setFavorite(false);
                    oldData.add(data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.execSQL("DROP TABLE IF EXISTS TransportTable");
        return oldData;
    }

    private void removeOld(SupportSQLiteDatabase db) {
        //Remove support from previous databases
        Log.i(getClass().getName(), "Removing old databases");
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
