package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.ConferenceModel;

import java.util.ArrayList;
import java.util.List;

public class ConferenceData {

    //Public Constants
    public static final String TABLE_CONFERENCE = "table_conference";
    public static final String CONFERENCE_KEY_ID = "conference_id";
    public static final String CONFERENCE_TIMESTAMP = "conference_timestamp";
    public static final String CONFERENCE_CODE = "conference_code";
    public static final String CONFERENCE_EVENT_ID = "conference_eventID";
    public static final String CONFERENCE_EVENT_TITLE = "conference_event_title";
    public static final String CONFERENCE_EVENT_SPEAKER = "conference_event_speaker";
    public static final String CONFERENCE_EVENT_HOST = "conference_event_host";
    public static final String CONFERENCE_EVENT_START_TIME = "conference_event_startTime";
    public static final String CONFERENCE_EVENT_END_TIME = "conference_event_endTime";
    public static final String CONFERENCE_EVENT_DATE = "conference_event_date";
    public static final String CONFERENCE_EVENT_VENUE = "conference_event_venue";
    public static final String CONFERENCE_EVENT_MESSAGE = "conference_event_message";
    public static final String CONFERENCE_EVENT_CODE = "conference_event_code";
    public static final String CONFERENCE_UPDATE_COUNTER = "conference_update_counter";


    SQLiteDatabase db;

    public ConferenceData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public void add(ConferenceModel entry) {
        ContentValues values = new ContentValues();
        values.put(CONFERENCE_TIMESTAMP, entry.getTimestamp());
        values.put(CONFERENCE_CODE, entry.getCode());
        values.put(CONFERENCE_EVENT_ID, entry.getEventID());
        values.put(CONFERENCE_EVENT_TITLE, entry.getEventTitle());
        values.put(CONFERENCE_EVENT_SPEAKER, entry.getEventSpeaker());
        values.put(CONFERENCE_EVENT_HOST, entry.getEventHost());
        values.put(CONFERENCE_EVENT_START_TIME, entry.getEventStartTime());
        values.put(CONFERENCE_EVENT_END_TIME, entry.getEventEndTime());
        values.put(CONFERENCE_EVENT_DATE, entry.getEventDate());
        values.put(CONFERENCE_EVENT_VENUE, entry.getEventVenue());
        values.put(CONFERENCE_EVENT_MESSAGE, entry.getEventMessage());
        values.put(CONFERENCE_EVENT_CODE, entry.getEventCode());
        values.put(CONFERENCE_UPDATE_COUNTER, entry.getUpdateCounter());
        db.insert(TABLE_CONFERENCE, null, values);
        db.close();
    }

    public List<ConferenceModel> getAll() {
        List<ConferenceModel> fullList = new ArrayList<ConferenceModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONFERENCE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ConferenceModel enrty = new ConferenceModel();
                enrty.setId(Integer.parseInt(cursor.getString(0)));
                enrty.setTimestamp(cursor.getString(1));
                enrty.setCode(cursor.getString(2));
                enrty.setEventID(cursor.getString(3));
                enrty.setEventTitle(cursor.getString(4));
                enrty.setEventSpeaker(cursor.getString(5));
                enrty.setEventHost(cursor.getString(6));
                enrty.setEventStartTime(cursor.getString(7));
                enrty.setEventEndTime(cursor.getString(8));
                enrty.setEventDate(cursor.getString(9));
                enrty.setEventVenue(cursor.getString(10));
                enrty.setEventMessage(cursor.getString(11));
                enrty.setEventCode(cursor.getString(12));
                enrty.setUpdateCounter(Integer.parseInt(cursor.getString(13)));
                fullList.add(enrty);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fullList;
    }

    public int update(ConferenceModel entry) {

        ContentValues values = new ContentValues();
        values.put(CONFERENCE_TIMESTAMP, entry.getTimestamp());
        values.put(CONFERENCE_CODE, entry.getCode());
        values.put(CONFERENCE_EVENT_ID, entry.getEventID());
        values.put(CONFERENCE_EVENT_TITLE, entry.getEventTitle());
        values.put(CONFERENCE_EVENT_SPEAKER, entry.getEventSpeaker());
        values.put(CONFERENCE_EVENT_HOST, entry.getEventHost());
        values.put(CONFERENCE_EVENT_START_TIME, entry.getEventStartTime());
        values.put(CONFERENCE_EVENT_END_TIME, entry.getEventEndTime());
        values.put(CONFERENCE_EVENT_DATE, entry.getEventDate());
        values.put(CONFERENCE_EVENT_VENUE, entry.getEventVenue());
        values.put(CONFERENCE_EVENT_MESSAGE, entry.getEventMessage());
        values.put(CONFERENCE_EVENT_CODE, entry.getEventCode());
        values.put(CONFERENCE_UPDATE_COUNTER, entry.getUpdateCounter());

        return db.update(TABLE_CONFERENCE, values, CONFERENCE_KEY_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_CONFERENCE);
        db.close();
    }

    public void delete(ConferenceModel enrty) {
        db.delete(TABLE_CONFERENCE, CONFERENCE_KEY_ID + " = ?", new String[]{String.valueOf(enrty.getId())});
        db.close();
    }

}
