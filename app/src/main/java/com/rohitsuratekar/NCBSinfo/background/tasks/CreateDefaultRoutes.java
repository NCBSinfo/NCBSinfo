package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Simple AsyncTask handles creation of default transport routes
 */

public class CreateDefaultRoutes extends AsyncTask<Object, Void, Void> {

    private OnTaskCompleted taskCompleted;

    public CreateDefaultRoutes(OnTaskCompleted taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    @Override
    protected Void doInBackground(Object... params) {
        Context context = (Context) params[0];
        new RouteData(context).clearAll();

        //Use default Routes
        //Maintain the order because their route numbers will decided based on how you add them in database

        List<RouteModel> allRoutes = new ArrayList<>();

        //Route 0
        allRoutes.add(make("ncbs", "iisc", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_iisc_week))));

        allRoutes.add(make("ncbs", "iisc", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_iisc_sunday))));

        //Route 1
        allRoutes.add(make("iisc", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_iisc_ncbs_week))));

        allRoutes.add(make("iisc", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_iisc_ncbs_sunday))));

        //Route 2
        allRoutes.add(make("ncbs", "mandara", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_mandara_week))));

        allRoutes.add(make("ncbs", "mandara", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_mandara_sunday))));

        //Route 3
        allRoutes.add(make("mandara", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_mandara_ncbs_week))));

        allRoutes.add(make("mandara", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_mandara_ncbs_sunday))));

        //Route 4
        allRoutes.add(make("ncbs", "mandara", Calendar.MONDAY, TransportType.BUGGY,
                Helper.convertStringToList(context.getString(R.string.def_buggy_from_ncbs))));

        //Route 5
        allRoutes.add(make("mandara", "ncbs", Calendar.MONDAY, TransportType.BUGGY,
                Helper.convertStringToList(context.getString(R.string.def_buggy_from_mandara))));

        //Route 6
        allRoutes.add(make("ncbs", "icts", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_icts_week))));

        allRoutes.add(make("ncbs", "icts", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_icts_sunday))));

        //Route 7
        allRoutes.add(make("icts", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_icts_ncbs_week))));

        allRoutes.add(make("icts", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_icts_ncbs_sunday))));

        //Route 8
        allRoutes.add(make("ncbs", "cbl", Calendar.SUNDAY, TransportType.TTC,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_cbl))));


        for (RouteModel r : allRoutes) {
            new RouteData(context).add(r);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        taskCompleted.onTaskCompleted();
    }


    private static final String DEFAULT_TRIGGER = "none";

    private RouteModel make(String origin, String destination, int day, TransportType type, List<String> trips) {
        RouteModel route = new RouteModel();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDay(day);
        route.setTrips(trips);
        route.setCreatedOn("2016-12-30 03:03:03");
        route.setAuthor("Default");
        route.setType(type);
        route.setRoute(0); // Handle route no into sqlight methods
        route.setTrigger(DEFAULT_TRIGGER);
        route.setNotes("");
        return route;

    }

}
