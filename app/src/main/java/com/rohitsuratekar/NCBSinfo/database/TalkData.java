package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import java.util.ArrayList;
import java.util.List;

public class TalkData {

    //Public constants
    public static final String TABLE_OLD_TALK = "table_talkdata"; //Old table
    public static final String TABLE_TALK = "table_research_talk";
    public static final String TALK_KEY_ID = "talk_id";
    public static final String TALK_TIMESTAMP = "talk_timestamp";
    public static final String TALK_NOTIFICATION_TITLE = "talk_notificationTitle";
    public static final String TALK_DATE = "talk_date";
    public static final String TALK_TIME = "talk_time";
    public static final String TALK_VENUE = "talk_venue";
    public static final String TALK_SPEAKER = "talk_speaker";
    public static final String TALK_AFFILICATION = "talk_talkabstract";
    public static final String TALK_TITLE = "talk_url";
    public static final String TALK_HOST = "talk_nextspeaker";
    public static final String TALK_DATACODE = "talk_talkcode";
    public static final String TALK_DATA_ACTION = "talk_data_action";
    public static final String TALK_ACTIONCODE = "talk_actioncode";

    SQLiteDatabase db;

    public TalkData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public void addEntry(TalkModel entry) {
        ContentValues values = new ContentValues();
        values.put(TALK_TIMESTAMP, entry.getTimestamp());
        values.put(TALK_NOTIFICATION_TITLE, entry.getNotificationTitle());
        values.put(TALK_DATE, entry.getDate());
        values.put(TALK_TIME, entry.getTime());
        values.put(TALK_VENUE, entry.getVenue());
        values.put(TALK_SPEAKER, entry.getSpeaker());
        values.put(TALK_AFFILICATION, entry.getAffilication());
        values.put(TALK_TITLE, entry.getTitle());
        values.put(TALK_HOST, entry.getHost());
        values.put(TALK_DATACODE, entry.getDataCode());
        values.put(TALK_ACTIONCODE, entry.getActionCode());
        values.put(TALK_DATA_ACTION, entry.getDataAction());
        db.insert(TABLE_TALK, null, values);
        db.close();
    }

    // Getting single entry
    public TalkModel getEntry(int id) {
        Cursor cursor = db.query(TABLE_TALK, new String[]{TALK_KEY_ID, TALK_TIMESTAMP, TALK_NOTIFICATION_TITLE, TALK_DATE,
                        TALK_TIME, TALK_VENUE, TALK_SPEAKER, TALK_AFFILICATION, TALK_TITLE, TALK_HOST, TALK_DATACODE, TALK_ACTIONCODE, TALK_DATA_ACTION}, TALK_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        TalkModel entry = new TalkModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11), cursor.getString(12));
        cursor.close();
        db.close();
        return entry;
    }

    // Getting All Database entries
    public List<TalkModel> getAll() {
        List<TalkModel> entryList = new ArrayList<TalkModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_TALK;
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
                model.setDataCode(cursor.getString(10));
                model.setActionCode(cursor.getInt(11));
                model.setDataAction(cursor.getString(12));
                // Adding database to list
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    // Updating single entry
    public void update(TalkModel entry) {
        ContentValues values = new ContentValues();
        values.put(TALK_TIMESTAMP, entry.getTimestamp());
        values.put(TALK_NOTIFICATION_TITLE, entry.getNotificationTitle());
        values.put(TALK_DATE, entry.getDate());
        values.put(TALK_TIME, entry.getTime());
        values.put(TALK_VENUE, entry.getVenue());
        values.put(TALK_SPEAKER, entry.getSpeaker());
        values.put(TALK_AFFILICATION, entry.getAffilication());
        values.put(TALK_TITLE, entry.getTitle());
        values.put(TALK_HOST, entry.getHost());
        values.put(TALK_DATACODE, entry.getDataCode());
        values.put(TALK_ACTIONCODE, entry.getActionCode());
        values.put(TALK_DATA_ACTION, entry.getDataAction());
        db.update(TABLE_TALK, values, TALK_KEY_ID + " = ?", new String[]{String.valueOf(entry.getDataID())});
        db.close();
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_TALK);
        db.close();
    }


    // Deleting single data entry
    public void delete(TalkModel data) {
        db.delete(TABLE_TALK, TALK_KEY_ID + " = ?", new String[]{String.valueOf(data.getDataID())});
        db.close();
    }

    // Getting single entry by timestamp
    public TalkModel getEntry(String timestamp) {
        Cursor cursor = db.query(TABLE_TALK, new String[]{TALK_KEY_ID, TALK_TIMESTAMP, TALK_NOTIFICATION_TITLE, TALK_DATE,
                        TALK_TIME, TALK_VENUE, TALK_SPEAKER, TALK_AFFILICATION, TALK_TITLE, TALK_HOST, TALK_DATACODE, TALK_ACTIONCODE, TALK_DATA_ACTION}, TALK_TIMESTAMP + "=?",
                new String[]{String.valueOf(timestamp)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        TalkModel entry = new TalkModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11), cursor.getString(12));
        cursor.close();
        db.close();
        return entry;
    }

    //Delete old table
    public void dropOldtable(){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OLD_TALK);
        db.close();
    }
}
