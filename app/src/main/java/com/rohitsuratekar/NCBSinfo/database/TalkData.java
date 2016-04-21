package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;

import java.util.ArrayList;
import java.util.List;


public class TalkData {


    public void addTalkEntry(SQLiteDatabase db, TalkModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.TALK_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.TALK_NOTIFICATION_TITLE, entry.getNotificationTitle());
        values.put(SQL.TALK_DATE, entry.getDate());
        values.put(SQL.TALK_TIME, entry.getTime());
        values.put(SQL.TALK_VENUE, entry.getVenue());
        values.put(SQL.TALK_SPEAKER, entry.getSpeaker());
        values.put(SQL.TALK_AFFILICATION, entry.getAffilication());
        values.put(SQL.TALK_TITLE, entry.getTitle());
        values.put(SQL.TALK_HOST, entry.getHost());
        values.put(SQL.TALK_DATACODE, entry.getDatacode());
        values.put(SQL.TALK_ACTIONCODE, entry.getActioncode());
        db.insert(SQL.TABLE_TALK, null, values);
        db.close();
    }

    // Getting single entry
    public TalkModel getTalkDataEntry(SQLiteDatabase db, int id) {
        Cursor cursor = db.query(SQL.TABLE_TALK, new String[]{SQL.TALK_KEY_ID, SQL.TALK_TIMESTAMP, SQL.TALK_NOTIFICATION_TITLE, SQL.TALK_DATE,
                        SQL.TALK_TIME, SQL.TALK_VENUE, SQL.TALK_SPEAKER, SQL.TALK_AFFILICATION, SQL.TALK_TITLE, SQL.TALK_HOST, SQL.TALK_DATACODE, SQL.TALK_ACTIONCODE}, SQL.TALK_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        TalkModel entry = new TalkModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11));
        cursor.close();
        db.close();
        return entry;
    }

    // Getting All Database entries
    public List<TalkModel> getTalkDatabase(SQLiteDatabase db) {
        List<TalkModel> entryList = new ArrayList<TalkModel>();
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_TALK;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                TalkModel model = new TalkModel();
                model.setDataID(Integer.parseInt(cursor.getString(0)));
                model.setTimestamp(cursor.getString(1));
                model.setNotificationTitle(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));
                model.setVenue(cursor.getString(5));
                model.setSpeaker(cursor.getString(6));
                model.setAffilication(cursor.getString(7));
                model.setTitle(cursor.getString(8));
                model.setHost(cursor.getString(9));
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
    public void updateTalkEntry(SQLiteDatabase db, TalkModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.TALK_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.TALK_NOTIFICATION_TITLE, entry.getNotificationTitle());
        values.put(SQL.TALK_DATE, entry.getDate());
        values.put(SQL.TALK_TIME, entry.getTime());
        values.put(SQL.TALK_VENUE, entry.getVenue());
        values.put(SQL.TALK_SPEAKER, entry.getSpeaker());
        values.put(SQL.TALK_AFFILICATION, entry.getAffilication());
        values.put(SQL.TALK_TITLE, entry.getTitle());
        values.put(SQL.TALK_HOST, entry.getHost());
        values.put(SQL.TALK_DATACODE, entry.getDatacode());
        values.put(SQL.TALK_ACTIONCODE, entry.getActioncode());
        db.update(SQL.TABLE_TALK, values, SQL.TALK_KEY_ID + " = ?", new String[]{String.valueOf(entry.getDataID())});
        db.close();
    }

    // Delete all data
    public void clearTalkDatabase(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + SQL.TABLE_TALK);
        db.close();
    }

    public int getTalkIDbyTimeStamp(SQLiteDatabase db, String timestamp){
        Cursor cursor = db.query(SQL.TABLE_TALK, new String[]{SQL.TALK_KEY_ID, SQL.TALK_TIMESTAMP, SQL.TALK_NOTIFICATION_TITLE, SQL.TALK_DATE,
                        SQL.TALK_TIME, SQL.TALK_VENUE, SQL.TALK_SPEAKER, SQL.TALK_AFFILICATION, SQL.TALK_TITLE, SQL.TALK_HOST, SQL.TALK_DATACODE, SQL.TALK_ACTIONCODE}, SQL.TALK_TIMESTAMP + "=?",
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
    public void deleteTalkEntry(SQLiteDatabase db, TalkModel data) {
        db.delete(SQL.TABLE_TALK, SQL.TALK_KEY_ID + " = ?", new String[]{String.valueOf(data.getDataID())});
        db.close();
    }
}