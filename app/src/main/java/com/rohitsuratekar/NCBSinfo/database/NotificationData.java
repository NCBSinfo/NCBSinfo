package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationData {

    //Public constants
    //Do not change names. If you want, create new table and copy data from here
    public static final String TABLE_NOTIFICATIONS = "table_notifications";
    public static final String KEY_ID = "notification_id";
    public static final String TIMESTAMP = "notification_timestamp";
    public static final String TITLE = "notification_title";
    public static final String MESSAGE = "notification_message";
    public static final String FROM = "notification_from";
    public static final String EXTRA_VARIABLES = "notification_extravariables";

    SQLiteDatabase db;

    public NotificationData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public void add(NotificationModel notification) {
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, notification.getTimestamp());
        values.put(TITLE, notification.getTitle());
        values.put(MESSAGE, notification.getMessage());
        values.put(FROM, notification.getFrom());
        values.put(EXTRA_VARIABLES, notification.getExtraVariables());
        // Inserting Row
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    public NotificationModel get(int id) {
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[] { KEY_ID, TIMESTAMP, TITLE, MESSAGE, FROM, EXTRA_VARIABLES }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();

        }
        NotificationModel notification = new NotificationModel(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        cursor.close();
        db.close();
        return notification;
    }

    public List<NotificationModel> getAll() {
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setId(Integer.parseInt(cursor.getString(0)));
                notificationModel.setTimestamp(cursor.getString(1));
                notificationModel.setTitle(cursor.getString(2));
                notificationModel.setMessage(cursor.getString(3));
                notificationModel.setFrom(cursor.getString(4));
                notificationModel.setExtraVariables(cursor.getString(5));
                // Adding contact to list
                notificationModelList.add(notificationModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        db.close();
        return notificationModelList;
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_NOTIFICATIONS);
        db.close();
    }

    // Deleting single contact
    public void delete(NotificationModel notification) {
        db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?", new String[] { String.valueOf(notification.getId()) });
        db.close();
    }


}
