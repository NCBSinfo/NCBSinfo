package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.ConferenceModel;

import java.util.ArrayList;
import java.util.List;

public class ConferenceData extends Database {

    SQLiteDatabase db;
    public ConferenceData(Context context) {
        super(context);
        db = getWritableDatabase();
    }

    public void add(ConferenceModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.CONFERENCE_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.CONFERENCE_CODE, entry.getCode());
        values.put(SQL.CONFERENCE_EVENT_ID, entry.getEventID());
        values.put(SQL.CONFERENCE_EVENT_TITLE, entry.getEventTitle());
        values.put(SQL.CONFERENCE_EVENT_SPEAKER, entry.getEventSpeaker());
        values.put(SQL.CONFERENCE_EVENT_HOST, entry.getEventHost());
        values.put(SQL.CONFERENCE_EVENT_START_TIME, entry.getEventStartTime());
        values.put(SQL.CONFERENCE_EVENT_END_TIME, entry.getEventEndTime());
        values.put(SQL.CONFERENCE_EVENT_DATE, entry.getEventDate());
        values.put(SQL.CONFERENCE_EVENT_VENUE, entry.getEventVenue());
        values.put(SQL.CONFERENCE_EVENT_MESSAGE, entry.getEventMessage());
        values.put(SQL.CONFERENCE_EVENT_CODE, entry.getEventCode());
        values.put(SQL.CONFERENCE_UPDATE_COUNTER, entry.getUpdateCounter());
        db.insert(SQL.TABLE_CONFERENCE, null, values);
        db.close();
    }

    public List<ConferenceModel> getAll() {
        List<ConferenceModel> fullList = new ArrayList<ConferenceModel>();
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_CONFERENCE;
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
        values.put(SQL.CONFERENCE_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.CONFERENCE_CODE, entry.getCode());
        values.put(SQL.CONFERENCE_EVENT_ID, entry.getEventID());
        values.put(SQL.CONFERENCE_EVENT_TITLE, entry.getEventTitle());
        values.put(SQL.CONFERENCE_EVENT_SPEAKER, entry.getEventSpeaker());
        values.put(SQL.CONFERENCE_EVENT_HOST, entry.getEventHost());
        values.put(SQL.CONFERENCE_EVENT_START_TIME, entry.getEventStartTime());
        values.put(SQL.CONFERENCE_EVENT_END_TIME, entry.getEventEndTime());
        values.put(SQL.CONFERENCE_EVENT_DATE, entry.getEventDate());
        values.put(SQL.CONFERENCE_EVENT_VENUE, entry.getEventVenue());
        values.put(SQL.CONFERENCE_EVENT_MESSAGE, entry.getEventMessage());
        values.put(SQL.CONFERENCE_EVENT_CODE, entry.getEventCode());
        values.put(SQL.CONFERENCE_UPDATE_COUNTER, entry.getUpdateCounter());

        return db.update(SQL.TABLE_CONFERENCE, values, SQL.CONFERENCE_KEY_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) });
    }

    public void delete(ConferenceModel enrty) {
        db.delete(SQL.TABLE_CONFERENCE, SQL.CONFERENCE_KEY_ID + " = ?", new String[] { String.valueOf(enrty.getId()) });
        db.close();
    }

}
