package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.LogModel;

import java.util.ArrayList;
import java.util.List;

public class LogData {

    // Adding new log
    public void addLogEntry(SQLiteDatabase db, LogModel log) {
        ContentValues values = new ContentValues();
        values.put(SQL.LOG_TIMESTAMP, log.getTimestamp());
        values.put(SQL.LOG_MESSAGE, log.getMessage());
        values.put(SQL.LOG_DETAILS, log.getDetails());
        values.put(SQL.LOG_CATEGORY, log.getCategory());
        values.put(SQL.LOG_STATUSCODE, log.getStatuscode());
        values.put(SQL.LOG_STATUS, log.getStatus());
        db.insert(SQL.TABLE_LOG, null, values);
    }

    //Getting All Categories
    public List<LogModel> getAllLogEntries (SQLiteDatabase db) {
        List<LogModel> entryList = new ArrayList<LogModel>();
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_LOG;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                LogModel model = new LogModel();
                model.setId(cursor.getInt(0));
                model.setTimestamp(cursor.getString(1));
                model.setMessage(cursor.getString(2));
                model.setDetails(cursor.getString(3));
                model.setCategory(cursor.getString(4));
                model.setStatuscode(cursor.getInt(5));
                model.setStatus(cursor.getInt(6));
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    // Deleting single log
    public void deleteLogEntry(SQLiteDatabase db, LogModel log) {
        db.delete(SQL.TABLE_LOG, SQL.LOG_ID + " = ?", new String[]{String.valueOf(log.getId())});
        db.close();
    }

    // Delete all logs
    public void clearLogs(SQLiteDatabase db) {
        db.execSQL("DELETE FROM "+SQL.TABLE_LOG);
        db.close();
    }

}
