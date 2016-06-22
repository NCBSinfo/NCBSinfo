package com.rohitsuratekar.NCBSinfo.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import java.util.ArrayList;
import java.util.List;

public class DataMigration {

    SQLiteDatabase db;
    Context context;

    public DataMigration(Context context) {
        Database db = new Database(context);
        this.context = context;
        this.db = db.getWritableDatabase();
    }

    public void migrateTalkTable() {

        List<TalkModel> oldData = getOldTalks();

        for (TalkModel old : oldData) {
            TalkModel model = new TalkModel();
            model.setDataID(old.getDataID());
            model.setTimestamp(old.getTimestamp());
            model.setNotificationTitle(old.getNotificationTitle());
            model.setDate(old.getDate());
            model.setTime(old.getTime());
            model.setVenue(old.getVenue());
            model.setSpeaker(old.getSpeaker());
            model.setAffilication(old.getAffilication());
            model.setTitle(old.getTitle());
            model.setHost(old.getHost());
            model.setDataCode(old.getDataCode());
            model.setActionCode(old.getActionCode());
            model.setDataAction("send");
            new TalkData(context).addEntry(model);
        }

        db.execSQL("DROP TABLE IF EXISTS 'table_talkdata'");

    }


    // Getting All Database entries
    private List<TalkModel> getOldTalks() {
        List<TalkModel> entryList = new ArrayList<TalkModel>();
        String selectQuery = "SELECT  * FROM " + TalkData.TABLE_OLD_TALK;
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
                // Adding database to list
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

}
