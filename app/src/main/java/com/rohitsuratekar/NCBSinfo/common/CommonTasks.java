package com.rohitsuratekar.NCBSinfo.common;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.List;

public class CommonTasks extends IntentService {
    static final String TAG = "CommonTasks";

    private static final String SEND_FAVORITE_CHANGE = "com.rohitsuratekar.NCBSinfo.common.action.fav";
    private static final String DELETE_ROUTES = "com.rohitsuratekar.NCBSinfo.background.action.deleteRoutes";
    private static final String DELETE_TRIPS = "com.rohitsuratekar.NCBSinfo.background.action.deleteTrips";


    private static final String FAV_ROUTE = "fav_route";
    private static final String DEL_ORIGIN = "delOrigin";
    private static final String DEL_DESTINATION = "delDestination";
    private static final String DEL_TYPE = "delType";
    private static final String DEL_DAY = "delDay";


    public CommonTasks() {
        super("CommonTasks");
    }


    public static void sendFavoriteRoute(Context context, int routeID) {
        Log.i(TAG, "New favorite route");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SEND_FAVORITE_CHANGE);
        intent.putExtra(FAV_ROUTE, routeID);
        context.startService(intent);
    }

    public static void deleteRoute(Context context, String origin, String destination, String type) {
        Log.i(TAG, "Deleting route " + origin + destination + type);
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(DELETE_ROUTES);
        intent.putExtra(DEL_ORIGIN, origin);
        intent.putExtra(DEL_DESTINATION, destination);
        intent.putExtra(DEL_TYPE, type);
        context.startService(intent);
    }

    public static void deleteSpecificTrips(Context context, String origin, String destination, String type, int day) {
        Log.i(TAG, "Deleting specific trips");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(DELETE_TRIPS);
        intent.putExtra(DEL_ORIGIN, origin);
        intent.putExtra(DEL_DESTINATION, destination);
        intent.putExtra(DEL_TYPE, type);
        intent.putExtra(DEL_DAY, day);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SEND_FAVORITE_CHANGE.equals(action)) {
                changeFavRoute(intent.getIntExtra(FAV_ROUTE, -1));
            } else if (DELETE_ROUTES.equals(action)) {
                String o = intent.getStringExtra(DEL_ORIGIN);
                String d = intent.getStringExtra(DEL_DESTINATION);
                String t = intent.getStringExtra(DEL_TYPE);
                deleteRoute(o, d, t);
            } else if (DELETE_TRIPS.equals(action)) {
                String o = intent.getStringExtra(DEL_ORIGIN);
                String d = intent.getStringExtra(DEL_DESTINATION);
                String t = intent.getStringExtra(DEL_TYPE);
                int day = intent.getIntExtra(DEL_DAY, 0);
                deleteTrips(o, d, t, day);
            } else {
                reportError(action + " not found.");
            }
        }
    }


    private void changeFavRoute(int routeID) {
        if (routeID != -1) {
            AppData db = AppData.getDatabase(getApplicationContext());
            db.routes().removeAllFavorite();
            db.routes().setFavorite(routeID);
        } else {
            reportError("Unable to set favorite route");
        }
    }

    private void deleteRoute(String origin, String destination, String type) {
        if (origin != null && destination != null && type != null) {
            AppData db = AppData.getDatabase(getApplicationContext());
            int routeNo = db.routes().getRouteNo(origin, destination, type);
            if (routeNo != 0) {
                db.routes().deleteRouteByNumber(routeNo);
                db.trips().deleteTripsByRoute(routeNo);
            } else {
                reportError("No such route found " + origin + "-" + destination + " " + type);
            }
        } else {
            reportError("Unable to delete route " + origin + "-" + destination + " " + type);
        }
    }

    private void deleteTrips(String origin, String destination, String type, int day) {
        if (origin != null && destination != null && type != null) {
            AppData db = AppData.getDatabase(getApplicationContext());
            int routeNo = db.routes().getRouteNo(origin, destination, type);
            if (routeNo != 0) {
                Log.i(TAG, "Deleting trips for the day: " + day);
                int tripID = db.trips().getTripID(routeNo, day);
                db.trips().deleteTripsByTrip(tripID);

                List<TripData> leftData = db.trips().getTripsByRoute(routeNo);

                if (leftData.size() == 0) {
                    Log.i(TAG, "No Trips are left hence deleting route as well.");
                    db.routes().deleteRouteByNumber(routeNo);
                }

            } else {
                reportError("No such route found " + origin + "-" + destination + " " + type);
            }
        } else {
            reportError("Unable to delete route " + origin + "-" + destination + " " + type);
        }
    }

    private void reportError(String error) {
        Log.i(TAG, error);
        Crashlytics.log(error);
    }
}
