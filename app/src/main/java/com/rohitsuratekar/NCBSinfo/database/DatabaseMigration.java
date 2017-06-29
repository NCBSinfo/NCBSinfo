package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.database.Cursor;

import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class DatabaseMigration {

    List<RouteData> migrateToLatest(SupportSQLiteDatabase db) {
        //// TODO: 20-06-17
        List<RouteData> dataList = new ArrayList<>();
        Log.inform("Database migration started");
        Cursor cursor = db.query("SELECT * FROM routes", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Log.inform(cursor.getString(cursor.getColumnIndex("origin")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return dataList;
    }
}
