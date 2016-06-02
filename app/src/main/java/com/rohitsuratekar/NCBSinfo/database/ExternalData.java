package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.ExternalModel;

import java.util.ArrayList;
import java.util.List;

public class ExternalData extends Database {

    SQLiteDatabase db;

    public ExternalData(Context context) {
        super(context);
        db = getWritableDatabase();
    }

    public void add(ExternalModel entry) {
        ContentValues values = new ContentValues();
        values.put(SQL.EXTERNAL_TIMESTAMP, entry.getTimestamp());
        values.put(SQL.EXTERNAL_CODE, entry.getCode());
        values.put(SQL.EXTERNAL_TITLE, entry.getTitle());
        values.put(SQL.EXTERNAL_MESSAGE, entry.getMessage());
        values.put(SQL.EXTERNAL_EXTRA, entry.getExtra());
        // Inserting Row
        db.insert(SQL.TABLE_EXTERNAL, null, values);
        db.close(); // Closing database connection
    }

    public List<ExternalModel> getAll() {
        List<ExternalModel> fullList = new ArrayList<ExternalModel>();
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_EXTERNAL;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ExternalModel enrty = new ExternalModel();
                enrty.setId(Integer.parseInt(cursor.getString(0)));
                enrty.setTimestamp(cursor.getString(1));
                enrty.setCode(cursor.getString(2));
                enrty.setTitle(cursor.getString(3));
                enrty.setMessage(cursor.getString(4));
                enrty.setExtra(cursor.getString(5));
                fullList.add(enrty);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fullList;
    }

    public void deleteContact(ExternalModel enrty) {
        db.delete(SQL.TABLE_EXTERNAL, SQL.EXTERNAL_KEY_ID + " = ?", new String[] { String.valueOf(enrty.getId()) });
        db.close();
    }


}
