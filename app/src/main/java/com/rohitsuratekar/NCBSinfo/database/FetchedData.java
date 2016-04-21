package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.LogModel;

import java.util.ArrayList;
import java.util.List;

public class FetchedData {

    // Adding new entry
    public void addDataEntry(SQLiteDatabase db, DataModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.DATA_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.DATA_TITLE, entry.getNotificationTitle());
        values.put(SQL.DATA_DATE, entry.getDate());
        values.put(SQL.DATA_TIME, entry.getTime());
        values.put(SQL.DATA_VENUE, entry.getVenue());
        values.put(SQL.DATA_SPEAKER, entry.getSpeaker());
        values.put(SQL.DATA_ABSTRACT, entry.getTalkabstract());
        values.put(SQL.DATA_URL, entry.getUrl());
        values.put(SQL.DATA_NEXTSPEAKER, entry.getNextspeaker());
        values.put(SQL.DATA_DATACODE, entry.getDatacode());
        values.put(SQL.DATA_ACTIONCODE, entry.getActioncode());
        db.insert(SQL.TABLE_DATABASE, null, values);
        db.close();
    }

    // Getting single entry
    public DataModel getDatabaseEntry(SQLiteDatabase db ,int id) {
        Cursor cursor = db.query(SQL.TABLE_DATABASE, new String[]{SQL.DATA_KEY_ID, SQL.DATA_TIMESTAMP, SQL.DATA_TITLE, SQL.DATA_DATE,
                        SQL.DATA_TIME, SQL.DATA_VENUE,SQL.DATA_SPEAKER, SQL.DATA_ABSTRACT,SQL.DATA_URL,SQL.DATA_NEXTSPEAKER,SQL.DATA_DATACODE, SQL.DATA_ACTIONCODE}, SQL.DATA_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        DataModel entry = new DataModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11));
        cursor.close();
        db.close();
        return entry;
    }

    // Getting All Database entries
    public List<DataModel> getFullDatabase(SQLiteDatabase db) {
        List<DataModel> entryList = new ArrayList<DataModel>();
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_DATABASE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                DataModel model = new DataModel();
                model.setDataID(Integer.parseInt(cursor.getString(0)));
                model.setTimestamp(cursor.getString(1));
                model.setNotificationTitle(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));
                model.setVenue(cursor.getString(5));
                model.setSpeaker(cursor.getString(6));
                model.setTalkabstract(cursor.getString(7));
                model.setUrl(cursor.getString(8));
                model.setNextspeaker(cursor.getString(9));
                model.setDatacode(cursor.getString(10));
                model.setActioncode(cursor.getInt(11));
                // Adding database to list
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    // Updating single entry
    public void updateDataEntry(SQLiteDatabase db,DataModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.DATA_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.DATA_TITLE, entry.getNotificationTitle());
        values.put(SQL.DATA_DATE, entry.getDate());
        values.put(SQL.DATA_TIME, entry.getTime());
        values.put(SQL.DATA_VENUE, entry.getVenue());
        values.put(SQL.DATA_SPEAKER, entry.getSpeaker());
        values.put(SQL.DATA_ABSTRACT, entry.getTalkabstract());
        values.put(SQL.DATA_URL, entry.getUrl());
        values.put(SQL.DATA_NEXTSPEAKER, entry.getNextspeaker());
        values.put(SQL.DATA_DATACODE, entry.getDatacode());
        values.put(SQL.DATA_ACTIONCODE, entry.getActioncode());
        db.update(SQL.TABLE_DATABASE, values, SQL.DATA_KEY_ID + " = ?", new String[] { String.valueOf(entry.getDataID()) });
        db.close();
    }

    // Delete all data
    public void clearDatabase(SQLiteDatabase db) {
        db.execSQL("DELETE FROM "+SQL.TABLE_DATABASE);
        db.close();
    }

    public int getIDbyTimeStamp(SQLiteDatabase db, String timestamp){
        Cursor cursor = db.query(SQL.TABLE_DATABASE, new String[]{SQL.DATA_KEY_ID, SQL.DATA_TIMESTAMP, SQL.DATA_TITLE, SQL.DATA_DATE,
                        SQL.DATA_TIME, SQL.DATA_VENUE,SQL.DATA_SPEAKER, SQL.DATA_ABSTRACT,SQL.DATA_URL,SQL.DATA_NEXTSPEAKER,SQL.DATA_DATACODE, SQL.DATA_ACTIONCODE}, SQL.DATA_TIMESTAMP + "=?",
                new String[]{String.valueOf(timestamp)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        DataModel entry = new DataModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11));
        cursor.close();
        db.close();
        return entry.getDataID();
    }

    // Deleting single data entry
    public void deleteDataEntry(SQLiteDatabase db, DataModel data) {
        db.delete(SQL.TABLE_DATABASE, SQL.DATA_KEY_ID + " = ?", new String[]{String.valueOf(data.getDataID())});
        db.close();
    }

}
