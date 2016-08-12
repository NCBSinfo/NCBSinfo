package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.secretbiology.helpers.general.sql.Column;
import com.secretbiology.helpers.general.sql.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TalkData {

    //Public constants
    public static final String TABLE_TALK = "table_research_talk";
    public static final String KEY_ID = "talk_id";
    public static final String TIMESTAMP = "talk_timestamp";
    public static final String NOTIFICATION_TITLE = "talk_notificationTitle";
    public static final String DATE = "talk_date";
    public static final String TIME = "talk_time";
    public static final String VENUE = "talk_venue";
    public static final String SPEAKER = "talk_speaker";
    public static final String AFFILICATION = "talk_talkabstract";
    public static final String TITLE = "talk_url";
    public static final String HOST = "talk_nextspeaker";
    public static final String DATACODE = "talk_talkcode";
    public static final String ACTIONCODE = "talk_actioncode";
    public static final String DATA_ACTION = "talk_data_action";

    SQLiteDatabase db;
    Context context;
    Database database;
    private Table talkTable;
    private LinkedHashMap<String, Column> map;

    public TalkData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
        this.context = context;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(KEY_ID, Column.ColumnType.PRIMARY_INTEGER));
        columnList.add(new Column(TIMESTAMP, Column.ColumnType.TEXT));
        columnList.add(new Column(NOTIFICATION_TITLE, Column.ColumnType.TEXT));
        columnList.add(new Column(DATE, Column.ColumnType.TEXT));
        columnList.add(new Column(TIME, Column.ColumnType.TEXT));
        columnList.add(new Column(VENUE, Column.ColumnType.TEXT));
        columnList.add(new Column(SPEAKER, Column.ColumnType.TEXT));
        columnList.add(new Column(AFFILICATION, Column.ColumnType.TEXT));
        columnList.add(new Column(TITLE, Column.ColumnType.TEXT));
        columnList.add(new Column(HOST, Column.ColumnType.TEXT));
        columnList.add(new Column(DATACODE, Column.ColumnType.TEXT));
        columnList.add(new Column(ACTIONCODE, Column.ColumnType.INTEGER));
        columnList.add(new Column(DATA_ACTION, Column.ColumnType.TEXT));
        talkTable = new Table(db, TABLE_TALK, columnList);
        map = talkTable.getMap();
    }

    public void makeTable() {
        talkTable.make();
        database.closeDatabase();
    }

    public void add(TalkModel entry) {
        talkTable.addRow(putValues(entry));
        database.closeDatabase();
    }

    public TalkModel get(int id) {
        LinkedHashMap<String, Column> m = talkTable.getRowByValue(KEY_ID, id).getMap();
        TalkModel talk = getValues(m);
        database.closeDatabase();
        return talk;
    }

    // Getting single entry by timestamp
    public TalkModel get(String timestamp) {
        LinkedHashMap<String, Column> m = talkTable.getRowByValue(TIMESTAMP, timestamp).getMap();
        TalkModel talk = getValues(m);
        database.closeDatabase();
        return talk;
    }

    public List<TalkModel> getAll() {
        List<TalkModel> talkList = new ArrayList<>();
        for (LinkedHashMap<String, Column> m : talkTable.getAllRows()) {
            talkList.add(getValues(m));
        }

        database.closeDatabase();
        return talkList;
    }

    public int update(TalkModel entry) {
        int TempInt = (int) talkTable.update(putValues(entry), KEY_ID);
        database.closeDatabase();
        return TempInt;
    }


    // Delete all data
    public void clearAll() {
        talkTable.clearAll();
        database.closeDatabase();
    }

    // Deleting single entry
    public void delete(TalkModel entry) {
        talkTable.delete(KEY_ID, entry.getDataID());
        database.closeDatabase();
    }

    // Drop
    public void drop() {
        talkTable.drop();
        database.closeDatabase();
    }

    //All Events commands
    public void removeOld() {
        int currentLimit = new Preferences(context).user().getNumberOfEventsToKeep();
        talkTable.removeOldEntries(currentLimit, KEY_ID);
        database.closeDatabase();
    }


    private Column withValue(String columnName, Object value) {
        Column column = map.get(columnName);
        column.setData(value);
        return column;
    }

    private List<Column> putValues(TalkModel talk) {
        List<Column> newList = new ArrayList<>();
        newList.add(withValue(KEY_ID, talk.getDataID()));
        newList.add(withValue(TIMESTAMP, talk.getTimestamp()));
        newList.add(withValue(NOTIFICATION_TITLE, talk.getNotificationTitle()));
        newList.add(withValue(DATE, talk.getDate()));
        newList.add(withValue(TIME, talk.getTime()));
        newList.add(withValue(VENUE, talk.getVenue()));
        newList.add(withValue(SPEAKER, talk.getSpeaker()));
        newList.add(withValue(AFFILICATION, talk.getAffilication()));
        newList.add(withValue(TITLE, talk.getTitle()));
        newList.add(withValue(HOST, talk.getHost()));
        newList.add(withValue(DATACODE, talk.getDataCode()));
        newList.add(withValue(ACTIONCODE, talk.getActionCode()));
        newList.add(withValue(DATA_ACTION, talk.getDataAction()));
        return newList;
    }


    private TalkModel getValues(LinkedHashMap<String, Column> m) {
        TalkModel talk = new TalkModel();
        talk.setDataID((Integer) m.get(KEY_ID).getData());
        talk.setTimestamp((String) m.get(TIMESTAMP).getData());
        talk.setNotificationTitle((String) m.get(NOTIFICATION_TITLE).getData());
        talk.setDate((String) m.get(DATE).getData());
        talk.setTime((String) m.get(TIME).getData());
        talk.setVenue((String) m.get(VENUE).getData());
        talk.setSpeaker((String) m.get(SPEAKER).getData());
        talk.setAffilication((String) m.get(AFFILICATION).getData());
        talk.setTitle((String) m.get(TITLE).getData());
        talk.setHost((String) m.get(HOST).getData());
        talk.setDataCode((String) m.get(DATACODE).getData());
        talk.setActionCode((Integer) m.get(ACTIONCODE).getData());
        talk.setDataAction((String) m.get(DATA_ACTION).getData());
        return talk;
    }

}
