package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    //Database Constants
    private static String DATABASE_NAME = "NCBSinfo";
    private static int DATABASE_VERSION = 7; //Changed from 6 to 7 on 6 July 2016

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton pattern
    private static Database instance;

    private int mOpenCounter;

    private SQLiteDatabase mDatabase;


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

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //All currently available database
        new Tables(db).makeContactTable();
        new Tables(db).makeTalkTable();
        new Tables(db).makeConferenceTable();
        new Tables(db).makeNotificationTable();
        new Tables(db).makeAlarmTable();
    }

    //No "break" statements are given to upgrade database serials
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > 3) {
            switch (oldVersion) {
                case 4:
                    new Tables(db).makeConferenceTable();
                case 5:
                    new Tables(db).makeTalkTable();
                    new Tables(db).makeNotificationTable();
                    db.execSQL("DROP TABLE IF EXISTS 'table_log'"); //Removing log table from this version
                    db.execSQL("DROP TABLE IF EXISTS 'table_database'"); //Removing JC database table from this version
                    db.execSQL("DROP TABLE IF EXISTS 'table_external'"); //Removing External database table from this version
                    db.execSQL("DROP TABLE IF EXISTS 'table_talkdata'"); //Removing Old talk table
                case 6:
                    new Tables(db).makeAlarmTable();
            }
        }
        //Remove support from previous databases
        else {
            new Tables(db).dropAllTables();
            onCreate(db);
        }
    }


    public void restartDatabase() {
        new Tables(instance.openDatabase()).dropAllTables();
        onCreate(instance.openDatabase());
        instance.closeDatabase();
    }

    public boolean isAlreadyThere(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = instance.openDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            instance.closeDatabase();
            return false;
        }
        cursor.close();
        instance.closeDatabase();
        return true;
    }


}

