package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.secretbiology.helpers.general.sql.Column;
import com.secretbiology.helpers.general.sql.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private Table alarmTable;
    private LinkedHashMap<String, Column> map;

    public AlarmData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(KEY, Column.ColumnType.PRIMARY_INTEGER));
        columnList.add(new Column(ALARM_ID, Column.ColumnType.INTEGER));
        columnList.add(new Column(TYPE, Column.ColumnType.TEXT));
        columnList.add(new Column(TRIGGER, Column.ColumnType.TEXT));
        columnList.add(new Column(LEVEL, Column.ColumnType.TEXT));
        columnList.add(new Column(EXTRA_PARAMETER, Column.ColumnType.TEXT));
        columnList.add(new Column(EXTRA_VALUE, Column.ColumnType.TEXT));
        columnList.add(new Column(ALARM_TIME, Column.ColumnType.TEXT));
        columnList.add(new Column(ALARM_DATE, Column.ColumnType.TEXT));
        alarmTable = new Table(db, TABLE_ALARMS, columnList);
        map = alarmTable.getMap();
    }

    public void makeTable() {
        alarmTable.make();
        database.closeDatabase();
    }

    public void add(AlarmModel entry) {
        alarmTable.addRow(putValues(entry));
        database.closeDatabase();
    }

    public long addAndGetID(AlarmModel entry) {
        long value = alarmTable.addRow(putValues(entry));
        database.closeDatabase();
        return value;
    }

    public AlarmModel get(int id) {
        LinkedHashMap<String, Column> m = alarmTable.getRowByValue(KEY, id).getMap();
        AlarmModel alarm = getValues(m);
        database.closeDatabase();
        return alarm;
    }


    public List<AlarmModel> getAll() {
        List<AlarmModel> alarmList = new ArrayList<>();
        for (LinkedHashMap<String, Column> m : alarmTable.getAllRows()) {
            alarmList.add(getValues(m));
        }

        database.closeDatabase();
        return alarmList;
    }

    public int update(AlarmModel entry) {
        int TempInt = (int) alarmTable.update(putValues(entry), KEY);
        database.closeDatabase();
        return TempInt;
    }


    // Delete all data
    public void clearAll() {
        alarmTable.clearAll();
        database.closeDatabase();
    }

    // Deleting single entry
    public void delete(AlarmModel entry) {
        alarmTable.delete(KEY, entry.getId());
        database.closeDatabase();
    }

    // Drop
    public void drop() {
        alarmTable.drop();
        database.closeDatabase();
    }


    private Column withValue(String columnName, Object value) {
        Column column = map.get(columnName);
        column.setData(value);
        return column;
    }

    private List<Column> putValues(AlarmModel entry) {
        List<Column> newList = new ArrayList<>();
        newList.add(withValue(KEY, entry.getId()));
        newList.add(withValue(ALARM_ID, entry.getAlarmID()));
        newList.add(withValue(TYPE, entry.getType()));
        newList.add(withValue(TRIGGER, entry.getTrigger()));
        newList.add(withValue(LEVEL, entry.getLevel()));
        newList.add(withValue(EXTRA_PARAMETER, entry.getExtraParameter()));
        newList.add(withValue(EXTRA_VALUE, entry.getExtraValue()));
        newList.add(withValue(ALARM_TIME, entry.getAlarmTime()));
        newList.add(withValue(ALARM_DATE, entry.getAlarmDate()));
        return newList;
    }


    private AlarmModel getValues(LinkedHashMap<String, Column> m) {
        AlarmModel entry = new AlarmModel();
        entry.setId((Integer) m.get(KEY).getData());
        entry.setAlarmID((Integer) m.get(ALARM_ID).getData());
        entry.setType((String) m.get(TYPE).getData());
        entry.setTrigger((String) m.get(TRIGGER).getData());
        entry.setLevel((String) m.get(LEVEL).getData());
        entry.setExtraParameter((String) m.get(EXTRA_PARAMETER).getData());
        entry.setExtraValue((String) m.get(EXTRA_VALUE).getData());
        entry.setAlarmTime((String) m.get(ALARM_TIME).getData());
        entry.setAlarmDate((String) m.get(ALARM_DATE).getData());
        return entry;
    }

}
