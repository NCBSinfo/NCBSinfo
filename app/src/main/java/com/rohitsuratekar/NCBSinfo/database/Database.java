/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.database.Column;
import com.secretbiology.helpers.general.database.DatabaseManager;
import com.secretbiology.helpers.general.database.ObjectTable;
import com.secretbiology.helpers.general.database.Table;

import java.util.ArrayList;
import java.util.List;

import static com.rohitsuratekar.NCBSinfo.database.RouteManager.TRANSPORT_TABLE;

public class Database extends DatabaseManager {

    private static final String DATABASE_NAME = "NCBSinfo";
    private static final int DATABASE_VERSION = 2;

    public Database(Context context, String name, int version) {
        super(context, name, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        Table.make(TRANSPORT_TABLE, db, getTransportColumns());
        Log.inform(DATABASE_NAME + " database Created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Table.drop(TRANSPORT_TABLE, db);

        Table.make(TRANSPORT_TABLE, db, getTransportColumns());
        Log.inform(DATABASE_NAME + " database upgraded from " + oldVersion + " to " + newVersion);
    }

    public Table getTransports() {
        return new Table(this, TRANSPORT_TABLE, getTransportColumns());
    }

    private static List<Column> getTransportColumns() {
        //DO TO CHANGE ORDER
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(RouteManager.KEY, Column.TYPE.INTEGER_PRIMARY_KEY));
        columns.add(new Column(RouteManager.ROUTE, Column.TYPE.INTEGER));
        columns.add(new Column(RouteManager.ORIGIN, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.DESTINATION, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.DAY, Column.TYPE.INTEGER));
        columns.add(new Column(RouteManager.TRIPS, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.TYPE, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.CREATION, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.MODIFIED, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.SYNCED, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.AUTHOR, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.DATABASE_ID, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.TRIGGER, Column.TYPE.TEXT));
        columns.add(new Column(RouteManager.NOTES, Column.TYPE.TEXT));
        return columns;
    }


}