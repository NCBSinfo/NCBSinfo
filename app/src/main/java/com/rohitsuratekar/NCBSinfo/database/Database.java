/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.secretbiology.helpers.general.Log;

public class Database extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "NCBSinfo";
    private static int DATABASE_VERSION = 9; //Changed from 8 to 9 in version 44
    private Context context;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    private static Database instance;

    private int mOpenCounter;

    public SQLiteDatabase mDatabase;


    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1) {
            // Opening new database
            mDatabase = instance.getWritableDatabase();
        }
        return mDatabase;
    }

    synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RouteData.make(db);
        Log.inform("Database '" + DATABASE_NAME + "' is created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Remove support from previous databases
        db.execSQL("DROP TABLE IF EXISTS alarmTable");
        db.execSQL("DROP TABLE IF EXISTS table_contacts");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS table_research_talk");

        //Make new ones
        RouteData.make(db);
        Log.inform("Database '" + DATABASE_NAME + "' is upgraded from " + oldVersion + " to " + newVersion + " successfully.");
    }


}