package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.database.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.rohitsuratekar.NCBSinfo.activities.Helper.getType;

public class RouteData {

    private static final String TRANSPORT_TABLE = "TransportTable";
    private static final String KEY = "key";
    private static final String ROUTE = "route";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String DAY = "day";
    private static final String TRIPS = "trips";
    private static final String TYPE = "type";
    private static final String CREATION = "creation";
    private static final String MODIFIED = "modified";
    private static final String SYNCED = "synced";
    private static final String AUTHOR = "author";
    private static final String DATABASE_ID = "databaseID";
    private static final String TRIGGER = "trigger";
    private static final String NOTES = "notes";


    private SQLiteDatabase db;
    private Database database;

    public RouteData(Context context) {
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
        columnList.add(new String[]{KEY, "INTEGER PRIMARY KEY"});
        columnList.add(new String[]{ROUTE, "INTEGER"});
        columnList.add(new String[]{ORIGIN, "TEXT"});
        columnList.add(new String[]{DESTINATION, "TEXT"});
        columnList.add(new String[]{DAY, "INTEGER"});
        columnList.add(new String[]{TRIPS, "TEXT"});
        columnList.add(new String[]{TYPE, "TEXT"});
        columnList.add(new String[]{CREATION, "TEXT"});
        columnList.add(new String[]{MODIFIED, "TEXT"});
        columnList.add(new String[]{SYNCED, "TEXT"});
        columnList.add(new String[]{AUTHOR, "TEXT"});
        columnList.add(new String[]{DATABASE_ID, "TEXT"});
        columnList.add(new String[]{TRIGGER, "TEXT"});
        columnList.add(new String[]{NOTES, "TEXT"});
        db.execSQL(QueryBuilder.buildTableQuery(TRANSPORT_TABLE, columnList));
        Log.inform("Table '" + TRANSPORT_TABLE + "' is created.");
    }

    /**
     * Drops table
     *
     * @param db: SQLight database (do not close)
     */
    static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSPORT_TABLE);
        Log.inform("Table " + TRANSPORT_TABLE + " dropped.");
    }

    /**
     * Adds route
     * This function is little complicated because we will be using unique route numbers.
     *
     * @param model : RouteModel
     * @return : Unique Key or Updated key (depending on condition satisfied)
     */
    public int add(RouteModel model) {

        int alreadyRoute = existsRoute(model.getOrigin(), model.getDestination(), model.getType());
        //Check if any such route exists, if not, make new route
        if (alreadyRoute == -1) {
            Log.inform("Making new Route from " + model.getOrigin() + "-" + model.getDestination());
            model.setRoute(getNextRoute());
            int id = (int) db.insert(TRANSPORT_TABLE, null, putContentValues(model));
            database.closeDatabase();
            return id;
        }
        //Now check if specific day trips are available for existing route, if not, add new day
        else if (existsRouteDay(model.getOrigin(), model.getDestination(), model.getType(), model.getDay()) == -1) {
            Log.inform("Making new day for the route " + model.getOrigin() + "-" + model.getDestination());
            model.setRoute(alreadyRoute);
            int id = (int) db.insert(TRANSPORT_TABLE, null, putContentValues(model));
            database.closeDatabase();
            return id;
        }
        //Else update existing route
        else {
            model.setRoute(alreadyRoute);
            model.setKey(getRouteKeyID(model).getKey());
            Log.inform("Updating existing route " + model.getOrigin() + "-" + model.getDestination());
            return update(model);
        }

    }

    /**
     * Updates row
     *
     * @param model : Model (must contain key)
     * @return : Unique key. Will return 0 of key not found
     */
    public int update(RouteModel model) {
        model.setModifiedOn(General.timeStamp());
        int updated = db.update(TRANSPORT_TABLE, putContentValues(model), KEY + "= ?",
                new String[]{String.valueOf(model.getKey())});
        database.closeDatabase();
        return updated;
    }

    public RouteModel get(int key) {
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(KEY, String.valueOf(key)).build();
        Cursor cursor = db.rawQuery(query, null);
        RouteModel model = new RouteModel();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                model = convertRow(cursor);
            }
            cursor.close();
        }
        database.closeDatabase();
        return model;
    }

    /**
     * Deletes model
     *
     * @param model : Model with key
     */
    public void delete(RouteModel model) {
        Log.inform("Deleted " + model.getOrigin() + " " + model.getDestination() + " " + model.getType().toString());
        db.delete(TRANSPORT_TABLE, KEY + "= ?", new String[]{String.valueOf(model.getKey())});
        database.closeDatabase();
    }

    /**
     * Clears all routes. You must send broadcast after using this to make default routes.
     * Else this will give error in Transport Activity.
     */
    public void clearAll() {
        db.execSQL("DELETE FROM " + TRANSPORT_TABLE);
        database.closeDatabase();
    }

    /**
     * Checks if route exists with this criteria (do not close database. Close it from parent method)
     *
     * @param origin      : Origin of route
     * @param destination : Destination of route
     * @param type        : Type of route
     * @return : Route No. Returns -1 if route not found with given combination
     */
    private int existsRoute(String origin, String destination, TransportType type) {
        String query = new QueryBuilder()
                .selectColumn(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type.toString().toUpperCase())
                .build();
        Cursor cursor = db.rawQuery(query, null);
        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Public method of existsRoute with automatic database closure
     *
     * @param origin      : Origin
     * @param destination : Destination
     * @param type        : Type
     * @return : -1 if no such route
     */
    public int checkIfExistsRoute(String origin, String destination, TransportType type) {
        String query = new QueryBuilder()
                .selectColumn(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type.toString().toUpperCase())
                .build();
        Cursor cursor = db.rawQuery(query, null);
        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        database.closeDatabase();
        return result;
    }

    /**
     * More specific search than 'existsRoute' method (do not close database yet, close from its parent method)
     *
     * @param origin      : Origin of route
     * @param destination : Destination of route
     * @param type        : Type of route
     * @param day         : Day of route
     * @return : Route number if combination exists else -1
     */

    private int existsRouteDay(String origin, String destination, TransportType type, int day) {
        String query = new QueryBuilder()
                .selectColumn(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type.toString().toUpperCase())
                .and()
                .columnIsEqual(DAY, String.valueOf(day))
                .build();
        Cursor cursor = db.rawQuery(query, null);
        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        return result;
    }

    /***
     * Gets route model from database ID of existing route (Do not close database)
     */
    private RouteModel getRouteKeyID(RouteModel model) {
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, model.getOrigin())
                .and()
                .columnIsEqual(DESTINATION, model.getDestination())
                .and()
                .columnIsEqual(TYPE, model.getType().toString().toUpperCase())
                .and()
                .columnIsEqual(DAY, String.valueOf(model.getDay()))
                .build();
        Cursor cursor = db.rawQuery(query, null);
        RouteModel NewModel = new RouteModel();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                NewModel = convertRow(cursor);
            }
            cursor.close();
        }
        return NewModel;
    }

    /**
     * @return :Next route no. (Do not close database. Close it form parent method)
     */

    private int getNextRoute() {
        String query = new QueryBuilder()
                .selectMax(ROUTE)
                .fromTable(TRANSPORT_TABLE)
                .build();
        Cursor cursor = db.rawQuery(query, null);
        int maxItem = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maxItem = cursor.getInt(0) + 1;
            }
            cursor.close();
        }
        int rows = 0;
        // Following condition is needed to start route numbers from 0
        // (This is to support legacy preferences)
        // Just check if table is not empty
        if (maxItem < 2) {
            String query2 = new QueryBuilder()
                    .selectAll()
                    .fromTable(TRANSPORT_TABLE)
                    .build();
            Cursor cursor2 = db.rawQuery(query2, null);
            if (cursor2 != null) {
                if (cursor2.moveToFirst()) {
                    rows = cursor2.getCount();
                }
                cursor2.close();
            }
            if (rows == 0) {
                return 0;
            } else {
                return maxItem;
            }
        }
        return maxItem;

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
        Cursor cursor = db.rawQuery(query, null);
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
        database.closeDatabase();
        return names;
    }

    /**
     * Gets all routes with given criteria
     *
     * @param origin:     Origin of the route
     * @param destination : Destination of the route
     * @param type        : Type of the route
     * @return : List of routes with given criteria.
     */

    public List<RouteModel> getAllDays(String origin, String destination, TransportType type) {
        List<RouteModel> all = new ArrayList<>();
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TRANSPORT_TABLE)
                .whereColumnIsEqual(ORIGIN, origin)
                .and()
                .columnIsEqual(DESTINATION, destination)
                .and()
                .columnIsEqual(TYPE, type.toString().toUpperCase())
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

    /**
     * Gets all routes from database
     *
     * @return: List of RouteModels
     */

    public List<RouteModel> getAll() {
        List<RouteModel> all = new ArrayList<>();
        String query = new QueryBuilder()
                .selectAll()
                .fromTable(TRANSPORT_TABLE)
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

    public void updateSyncTime(String time) {
        String query = "UPDATE " + TRANSPORT_TABLE + " SET " + SYNCED + " = '" + time + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                Log.inform("Updated " + cursor.getCount() + "rows");
            }
            cursor.close();
        }
        database.closeDatabase();
    }

    /**
     * Private method to put all content values.
     * Note PRIMARY INTEGER KEY is NOT added into content values.
     * Also trips list is flatten to json string for storage and TransportType is converted to String
     *
     * @param model : Route Model
     * @return : Content values
     */

    private ContentValues putContentValues(RouteModel model) {
        ContentValues values = new ContentValues();
        values.put(ROUTE, model.getRoute());
        values.put(ORIGIN, model.getOrigin());
        values.put(DESTINATION, model.getDestination());
        values.put(DAY, model.getDay());
        values.put(TYPE, model.getType().toString().toUpperCase());
        values.put(TRIPS, new Gson().toJson(model.getTrips()));
        values.put(CREATION, model.getCreatedOn());
        values.put(MODIFIED, model.getModifiedOn());
        values.put(SYNCED, model.getSyncedOn());
        values.put(AUTHOR, model.getAuthor());
        values.put(DATABASE_ID, model.getDatabaseID());
        values.put(TRIGGER, model.getTrigger());
        values.put(NOTES, model.getNotes());
        return values;
    }

    /**
     * Private value to put cursor values into model
     *
     * @param cursor : Cursor from query (This cursor should come from "SELECT ALL" columns
     * @return : RouteModel with given cursor values
     */
    private RouteModel convertRow(Cursor cursor) {
        RouteModel m = new RouteModel();
        m.setKey(cursor.getInt(cursor.getColumnIndex(KEY)));
        m.setRoute(cursor.getInt(cursor.getColumnIndex(ROUTE)));
        m.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
        m.setDestination(cursor.getString(cursor.getColumnIndex(DESTINATION)));
        m.setDay(cursor.getInt(cursor.getColumnIndex(DAY)));
        m.setType(getType(cursor.getString(cursor.getColumnIndex(TYPE))));
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
}
