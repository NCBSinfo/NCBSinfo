package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.secretbiology.helpers.general.sql.Column;
import com.secretbiology.helpers.general.sql.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    Database database;
    private Table notificationTable;
    private LinkedHashMap<String, Column> map;

    public NotificationData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(KEY_ID, Column.ColumnType.PRIMARY_INTEGER));
        columnList.add(new Column(TIMESTAMP, Column.ColumnType.TEXT));
        columnList.add(new Column(TITLE, Column.ColumnType.TEXT));
        columnList.add(new Column(MESSAGE, Column.ColumnType.TEXT));
        columnList.add(new Column(FROM, Column.ColumnType.TEXT));
        columnList.add(new Column(EXTRA_VARIABLES, Column.ColumnType.TEXT));
        notificationTable = new Table(db, TABLE_NOTIFICATIONS, columnList);
        map = notificationTable.getMap();

    }

    public void makeTable() {
        notificationTable.make();
        database.closeDatabase();
    }

    public void add(NotificationModel notification) {
        notificationTable.addRow(putValues(notification));
        database.closeDatabase();
    }

    public NotificationModel get(int id) {
        LinkedHashMap<String, Column> m = notificationTable.getRowByValue(KEY_ID, id).getMap();
        NotificationModel notification = getValues(m);
        database.closeDatabase();
        return notification;
    }

    public List<NotificationModel> getAll() {
        List<NotificationModel> notificationList = new ArrayList<>();
        for (LinkedHashMap<String, Column> m : notificationTable.getAllRows()) {
            notificationList.add(getValues(m));
        }
        database.closeDatabase();
        return notificationList;
    }

    // Delete all data
    public void clearAll() {
        notificationTable.clearAll();
        database.closeDatabase();
    }

    // Deleting single entry
    public void delete(NotificationModel notificationModel) {
        notificationTable.delete(KEY_ID, notificationModel.getId());
        database.closeDatabase();
    }

    // Drop
    public void drop() {
        notificationTable.drop();
        database.closeDatabase();
    }

    private Column withValue(String columnName, Object value) {
        Column column = map.get(columnName);
        column.setData(value);
        return column;
    }

    private List<Column> putValues(NotificationModel notification) {
        List<Column> newList = new ArrayList<>();
        newList.add(withValue(KEY_ID, notification.getId()));
        newList.add(withValue(TIMESTAMP, notification.getTimestamp()));
        newList.add(withValue(TITLE, notification.getTitle()));
        newList.add(withValue(MESSAGE, notification.getMessage()));
        newList.add(withValue(FROM, notification.getFrom()));
        newList.add(withValue(EXTRA_VARIABLES, notification.getExtraVariables()));
        return newList;
    }

    private NotificationModel getValues(LinkedHashMap<String, Column> m) {
        NotificationModel notification = new NotificationModel();
        notification.setId((Integer) m.get(KEY_ID).getData());
        notification.setTimestamp((String) m.get(TIMESTAMP).getData());
        notification.setTitle((String) m.get(TITLE).getData());
        notification.setMessage((String) m.get(MESSAGE).getData());
        notification.setFrom((String) m.get(FROM).getData());
        notification.setExtraVariables((String) m.get(EXTRA_VARIABLES).getData());
        return notification;
    }


}
