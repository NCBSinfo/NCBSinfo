package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    //TODO fix database opening and closing
    //Database Constants
    private static String DATABASE_NAME = "NCBSinfo";
    private static int DATABASE_VERSION = 8; //Changed from 7 to 8 on 12 August 2016
    private Context context;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Singleton pattern
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

        //All currently available database
        ContactsData.makeTable(db);
        AlarmData.makeTable(db);
        NotificationData.makeTable(db);
        TalkData.makeTable(db);
    }

    //Upgrade will take time, hence don't use database synchronized objects here
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove support from previous databases
        Log.i("Database Tag", "Database upgrade started");
        db.execSQL("DROP TABLE IF EXISTS " + ContactsData.TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmData.TABLE_ALARMS);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationData.TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TalkData.TABLE_TALK);
        onCreate(db);
    }


    public void restartDatabase() {
        dropAll();
        SQLiteDatabase db = Database.getInstance(context).openDatabase();
        ContactsData.makeTable(db);
        AlarmData.makeTable(db);
        NotificationData.makeTable(db);
        TalkData.makeTable(db);
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

    private void dropAll() {
        new ContactsData(context).drop();
        new AlarmData(context).drop();
        new NotificationData(context).drop();
        new TalkData(context).drop();
    }


}

