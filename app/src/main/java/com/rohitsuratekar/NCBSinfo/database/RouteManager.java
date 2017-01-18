package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.database.Column;
import com.secretbiology.helpers.general.database.Row;
import com.secretbiology.helpers.general.database.Table;

import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    static final String TRANSPORT_TABLE = "TransportTable";
    static final String KEY = "key";
    static final String ROUTE = "route";
    static final String ORIGIN = "origin";
    static final String DESTINATION = "destination";
    static final String DAY = "day";
    static final String TRIPS = "trips";
    static final String TYPE = "type";
    static final String CREATION = "creation";
    static final String MODIFIED = "modified";
    static final String SYNCED = "synced";
    static final String AUTHOR = "author";
    static final String DATABASE_ID = "databaseID";
    static final String TRIGGER = "trigger";
    static final String NOTES = "notes";


    private Table table;

    public RouteManager(Context context) {
        this.table = new Database(context).getTransports();
    }

    public int existsRoute(String origin, String destination, String type) {
        String query = new QueryBuilder()
                .selectColumn(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type)
                .build();
        Cursor cursor = table.executeCustom(query);
        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        table.closeDatabase();
        return result;
    }

    public int existsRouteDay(String origin, String destination, String type, int day) {
        String query = new QueryBuilder()
                .selectColumn(KEY)
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type)
                .and()
                .columnIsEqual(DAY, String.valueOf(day))
                .build();
        Cursor cursor = table.executeCustom(query);
        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        table.closeDatabase();
        return result;
    }

    public int getNextRoute(String origin, String destination) {
        String query = new QueryBuilder()
                .selectMax(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .build();
        Cursor cursor = table.executeCustom(query);
        int returnItem = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                returnItem = cursor.getInt(0) + 1;
            }
            cursor.close();
        }
        table.closeDatabase();
        return returnItem;
    }

    public void add(RouteModel model) {

        int key = existsRouteDay(model.getOrigin(), model.getDestination(), model.getType(), model.getDay());
        if (key == -1) {
            Log.inform("Creating new route");
            table.add(convertModel(model));
        } else {
            Log.inform("Updating new route");
            table.update(convertModel(model), KEY, String.valueOf(key));
        }
    }

    public void clear() {
        //TODO : send broadcast to make default
        table.clearAll();
        Log.inform("Cleared all routes.");
    }

    public List<String[]> getRouteNames() {

        List<String[]> names = new ArrayList<>();

        String query = new QueryBuilder()
                .select()
                .columnList(ORIGIN, DESTINATION, TYPE)
                .fromTable(TRANSPORT_TABLE)
                .groupBy()
                .columnList(ORIGIN, DESTINATION, TYPE)
                .build();
        Cursor cursor = table.executeCustom(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    names.add(new String[]{cursor.getString(cursor.getColumnIndex(ORIGIN)),
                            cursor.getString(cursor.getColumnIndex(DESTINATION)),
                            cursor.getString(cursor.getColumnIndex(TYPE))});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        table.closeDatabase();
        return names;
    }

    public List<RouteModel> getAllDays(String origin, String destination, String type) {
        List<RouteModel> all = new ArrayList<>();
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type)
                .build();
        Cursor cursor = table.executeCustom(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    all.add(convertRow(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        table.closeDatabase();
        return all;
    }

    public RouteModel convertRow(Cursor cursor) {
        RouteModel m = new RouteModel();
        m.setKey(cursor.getInt(cursor.getColumnIndex(KEY)));
        m.setRoute(cursor.getInt(cursor.getColumnIndex(ROUTE)));
        m.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
        m.setDestination(cursor.getString(cursor.getColumnIndex(DESTINATION)));
        m.setDay(cursor.getInt(cursor.getColumnIndex(DAY)));
        m.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        m.setCreatedOn(cursor.getString(cursor.getColumnIndex(CREATION)));
        m.setSyncedOn(cursor.getString(cursor.getColumnIndex(SYNCED)));
        m.setModifiedOn(cursor.getString(cursor.getColumnIndex(MODIFIED)));
        m.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
        m.setTrigger(cursor.getString(cursor.getColumnIndex(TRIGGER)));
        m.setDatabaseID(cursor.getString(cursor.getColumnIndex(DATABASE_ID)));
        m.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));


        List<String> trips = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(TRIPS)),
                new TypeToken<List<String>>() {
                }.getType());

        m.setTrips(trips);
        return m;
    }


    public Row convertModel(RouteModel model) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(RouteManager.KEY, Column.TYPE.INTEGER_PRIMARY_KEY, model.getKey()));
        columns.add(new Column(RouteManager.ROUTE, Column.TYPE.INTEGER, model.getRoute()));
        columns.add(new Column(RouteManager.ORIGIN, Column.TYPE.TEXT, model.getOrigin()));
        columns.add(new Column(RouteManager.DESTINATION, Column.TYPE.TEXT, model.getDestination()));
        columns.add(new Column(RouteManager.DAY, Column.TYPE.INTEGER, model.getDay()));
        //Convert list to Json
        columns.add(new Column(RouteManager.TRIPS, Column.TYPE.TEXT, new Gson().toJson(model.getTrips())));
        columns.add(new Column(RouteManager.TYPE, Column.TYPE.TEXT, model.getType()));
        columns.add(new Column(RouteManager.CREATION, Column.TYPE.TEXT, model.getCreatedOn()));
        columns.add(new Column(RouteManager.MODIFIED, Column.TYPE.TEXT, model.getModifiedOn()));
        columns.add(new Column(RouteManager.SYNCED, Column.TYPE.TEXT, model.getSyncedOn()));
        columns.add(new Column(RouteManager.AUTHOR, Column.TYPE.TEXT, model.getAuthor()));
        columns.add(new Column(RouteManager.DATABASE_ID, Column.TYPE.TEXT, model.getDatabaseID()));
        columns.add(new Column(RouteManager.TRIGGER, Column.TYPE.TEXT, model.getTrigger()));
        columns.add(new Column(RouteManager.NOTES, Column.TYPE.TEXT, model.getNotes()));
        return new Row(columns);
    }


}
