package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    //Database Constants
    private static String DATABASE_NAME = "NCBSinfo";
    private static int DATABASE_VERSION = 6; //Changed from 5 to 6 on 21 June 2016

    Context mContext;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //All currently available database
        new Tables(db).makeContactTable();
        new Tables(db).makeTalkTable();
        new Tables(db).makeConferenceTable();
        new Tables(db).makeNotificationTable();
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
            }
        }
        //Remove support from previous databases
        else {
            new Tables(db).dropAllTables();
            onCreate(db);
        }
    }


    public void restartDatabase() {
        new Tables(getWritableDatabase()).dropAllTables();
        onCreate(getWritableDatabase());
    }

    public boolean isAlreadyThere(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }
}

