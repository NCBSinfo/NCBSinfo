package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.database.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class NotificationData {

    //Same as old version table

    private static final String TABLE = "table_notifications";
    private static final String KEY_ID = "notification_id";
    private static final String TIMESTAMP = "notification_timestamp";
    private static final String TITLE = "notification_title";
    private static final String MESSAGE = "notification_message";
    private static final String FROM = "notification_from";
    private static final String EXPIRES = "notification_expires";
    private static final String EXTRA_VARIABLES = "notification_extravariables";


    private SQLiteDatabase db;
    private Database database;

    public NotificationData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
    }

    /**
     * Creates table
     *
     * @param db : SQLight database (do not close)
     */
    static void make(SQLiteDatabase db) {
        List<String[]> columnList = new ArrayList<>();
        columnList.add(new String[]{KEY_ID, "INTEGER PRIMARY KEY"});
        columnList.add(new String[]{TIMESTAMP, "TEXT"});
        columnList.add(new String[]{TITLE, "TEXT"});
        columnList.add(new String[]{MESSAGE, "TEXT"});
        columnList.add(new String[]{FROM, "TEXT"});
        columnList.add(new String[]{EXPIRES, "TEXT"});
        columnList.add(new String[]{EXTRA_VARIABLES, "TEXT"});
        db.execSQL(QueryBuilder.buildTableQuery(TABLE, columnList));
        Log.inform("Table '" + TABLE + "' is created.");
    }

    /**
     * Gets all notifications from database
     *
     * @return: List of NotificationModel
     */

    public List<NotificationModel> getAll() {
        List<NotificationModel> all = new ArrayList<>();
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TABLE)
                .build();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    all.add(convertRow(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        database.closeDatabase();
        return all;
    }

    public int add(NotificationModel model) {
        int id = (int) db.insert(TABLE, null, putContentValues(model));
        database.closeDatabase();
        return id;
    }

    /**
     * Deletes model
     *
     * @param model : Model with key
     */
    public void delete(NotificationModel model) {
        db.delete(TABLE, KEY_ID + "= ?", new String[]{String.valueOf(model.getId())});
        database.closeDatabase();
    }

    /**
     * Clears all notifications.
     */
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE);
        database.closeDatabase();
    }


    /**
     * Private method to put all content values.
     * Note PRIMARY INTEGER KEY is NOT added into content values.
     *
     * @param model : NotificationModel Model
     * @return : Content values
     */

    private ContentValues putContentValues(NotificationModel model) {
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, model.getTimestamp());
        values.put(TITLE, model.getTitle());
        values.put(MESSAGE, model.getMessage());
        values.put(FROM, model.getFrom());
        values.put(EXPIRES, model.getExpires());
        values.put(EXTRA_VARIABLES, model.getExtraVariables());
        return values;
    }

    /**
     * Private value to put cursor values into model
     *
     * @param cursor : Cursor from query (This cursor should come from "SELECT ALL" columns
     * @return : NotificationModel with given cursor values
     */
    private NotificationModel convertRow(Cursor cursor) {
        NotificationModel m = new NotificationModel();
        m.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        m.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
        m.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        m.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
        m.setFrom(cursor.getString(cursor.getColumnIndex(FROM)));
        m.setExpires(cursor.getString(cursor.getColumnIndex(EXPIRES)));
        m.setExtraVariables(cursor.getString(cursor.getColumnIndex(EXTRA_VARIABLES)));
        return m;
    }

}
