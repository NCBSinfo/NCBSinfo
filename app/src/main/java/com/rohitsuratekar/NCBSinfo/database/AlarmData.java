package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class AlarmData implements AlarmConstants {

    //Public constants
    public static final String TABLE_ALARMS = "alarmTable";
    public static final String KEY = "alarm_key";
    public static final String ALARM_ID = "alarm_id";
    public static final String TYPE = "alarm_type";
    public static final String TRIGGER = "alarm_trigger";
    public static final String LEVEL = "alarm_level";
    public static final String EXTRA_PARAMETER = "alarm_extraParameter";
    public static final String EXTRA_VALUE = "alarm_extraValue";
    public static final String ALARM_TIME = "alarm_alarmTime"; //HH:mm
    public static final String ALARM_DATE = "alarm_alarmDate"; //dd/MM/yyyy


    SQLiteDatabase db;
    Database database;

    public AlarmData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
    }


    public void add(AlarmModel entry) {
        ContentValues values = new ContentValues();
        values.put(ALARM_ID, entry.getAlarmID());
        values.put(TYPE, entry.getType());
        values.put(TRIGGER, entry.getTrigger());
        values.put(LEVEL, entry.getLevel());
        values.put(EXTRA_PARAMETER, entry.getExtraParameter());
        values.put(EXTRA_VALUE, entry.getExtraValue());
        values.put(ALARM_TIME, entry.getAlarmTime());
        values.put(ALARM_DATE, entry.getAlarmDate());
        db.insert(TABLE_ALARMS, null, values);
        database.closeDatabase();
    }


    public List<AlarmModel> getAll() {
        List<AlarmModel> fullList = new ArrayList<AlarmModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                AlarmModel entry = new AlarmModel();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setAlarmID(Integer.parseInt(cursor.getString(1)));
                entry.setType(cursor.getString(2));
                entry.setTrigger(cursor.getString(3));
                entry.setLevel(cursor.getString(4));
                entry.setExtraParameter(cursor.getString(5));
                entry.setExtraValue(cursor.getString(6));
                entry.setAlarmTime(cursor.getString(7));
                entry.setAlarmDate(cursor.getString(8));
                fullList.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.closeDatabase();
        return fullList;
    }

    public int update(AlarmModel entry) {

        ContentValues values = new ContentValues();
        values.put(ALARM_ID, entry.getAlarmID());
        values.put(TYPE, entry.getType());
        values.put(TRIGGER, entry.getTrigger());
        values.put(LEVEL, entry.getLevel());
        values.put(EXTRA_PARAMETER, entry.getExtraParameter());
        values.put(EXTRA_VALUE, entry.getExtraValue());
        values.put(ALARM_TIME, entry.getAlarmTime());
        values.put(ALARM_DATE, entry.getAlarmDate());
        int returnID = db.update(TABLE_ALARMS, values, KEY + " = ?",
                new String[]{String.valueOf(entry.getId())});
        database.closeDatabase();
        return returnID;
    }


    public AlarmModel get(int id) {
        Cursor cursor = db.query(TABLE_ALARMS, new String[]{KEY, ALARM_ID, TYPE, TRIGGER, LEVEL, EXTRA_PARAMETER
                        , EXTRA_VALUE, ALARM_TIME, ALARM_DATE}, KEY + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        AlarmModel alarm = new AlarmModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                , cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        // return alarm
        cursor.close();
        database.closeDatabase();
        return alarm;
    }

    public AlarmModel getByAlarmID(int AlarmID) {
        Cursor cursor = db.query(TABLE_ALARMS, new String[]{KEY, ALARM_ID, TYPE, TRIGGER, LEVEL, EXTRA_PARAMETER
                        , EXTRA_VALUE, ALARM_TIME, ALARM_DATE}, ALARM_ID + "=?",
                new String[]{String.valueOf(AlarmID)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        AlarmModel alarm = new AlarmModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                , cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        // return contact
        cursor.close();
        database.closeDatabase();
        return alarm;
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_ALARMS);
        database.closeDatabase();
    }

    public void delete(AlarmModel enrty) {
        db.delete(TABLE_ALARMS, KEY + " = ?", new String[]{String.valueOf(enrty.getId())});
        database.closeDatabase();
    }


}
