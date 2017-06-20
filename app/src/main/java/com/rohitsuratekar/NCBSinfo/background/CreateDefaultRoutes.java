package com.rohitsuratekar.NCBSinfo.background;

import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class CreateDefaultRoutes extends AsyncTask<Void, Void, Void> {

    private OnFinish onFinish;
    private AppData db;
    private List<TempInfo> infoList = new ArrayList<>();
    private List<String> buggyToNCBS = new ArrayList<>();
    private List<String> buggyToMandara = new ArrayList<>();
    private List<String> ncbsToCBL = new ArrayList<>();


    public CreateDefaultRoutes(OnFinish onFinish, Context context) {
        this.onFinish = onFinish;
        this.db = AppData.getDatabase(context);
        infoList.add(new TempInfo(context, "ncbs", "iisc", "shuttle", R.string.def_ncbs_iisc_week, R.string.def_ncbs_iisc_sunday));
        infoList.add(new TempInfo(context, "iisc", "ncbs", "shuttle", R.string.def_iisc_ncbs_week, R.string.def_iisc_ncbs_sunday));
        infoList.add(new TempInfo(context, "ncbs", "mandara", "shuttle", R.string.def_ncbs_mandara_week, R.string.def_ncbs_mandara_sunday));
        infoList.add(new TempInfo(context, "mandara", "ncbs", "shuttle", R.string.def_mandara_ncbs_week, R.string.def_mandara_ncbs_sunday));
        infoList.add(new TempInfo(context, "ncbs", "icts", "shuttle", R.string.def_ncbs_icts_week, R.string.def_ncbs_icts_sunday));
        infoList.add(new TempInfo(context, "icts", "ncbs", "shuttle", R.string.def_icts_ncbs_week, R.string.def_icts_ncbs_sunday));


        buggyToNCBS = convertToList(context.getString(R.string.def_buggy_from_mandara));
        buggyToMandara = convertToList(context.getString(R.string.def_buggy_from_ncbs));
        ncbsToCBL = convertToList(context.getString(R.string.def_ncbs_cbl));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (TempInfo info : infoList) {
            db.routes().insertRoute(info.getData());
            int routeID = db.routes().getRouteNo(info.getData().getOrigin(), info.getData().getDestination(), info.getData().getType());
            TripData weekDay = new TripData();
            weekDay.setDay(Calendar.MONDAY);
            weekDay.setRouteID(routeID);
            weekDay.setTrips(info.getWeek());
            db.trips().insertTrips(weekDay);

            TripData sunday = new TripData();
            sunday.setDay(Calendar.SUNDAY);
            sunday.setRouteID(routeID);
            sunday.setTrips(info.getSunday());
            db.trips().insertTrips(sunday);
            Log.inform("Created route for " + info.getData().getOrigin() + "-" + info.getData().getDestination());
        }

        RouteData r1 = convertToRoute("ncbs", "mandara", "buggy");
        db.routes().insertRoute(r1);
        int r1_id = db.routes().getRouteNo(r1.getOrigin(), r1.getDestination(), r1.getType());
        TripData t1 = new TripData();
        t1.setRouteID(r1_id);
        t1.setTrips(buggyToMandara);
        t1.setDay(Calendar.MONDAY);
        db.trips().insertTrips(t1);

        RouteData r2 = convertToRoute("mandara", "ncbs", "buggy");
        db.routes().insertRoute(r2);
        int r2_id = db.routes().getRouteNo(r2.getOrigin(), r2.getDestination(), r2.getType());
        TripData t2 = new TripData();
        t2.setRouteID(r2_id);
        t2.setTrips(buggyToNCBS);
        t2.setDay(Calendar.MONDAY);
        db.trips().insertTrips(t2);

        Log.inform("Buggy route created");

        RouteData r3 = convertToRoute("ncbs", "cbl", "ttc");
        db.routes().insertRoute(r3);
        int r3_id = db.routes().getRouteNo(r3.getOrigin(), r3.getDestination(), r3.getType());
        TripData t3 = new TripData();
        t3.setRouteID(r3_id);
        t3.setTrips(ncbsToCBL);
        t3.setDay(Calendar.MONDAY);
        db.trips().insertTrips(t3);

        Log.inform("NCBS-CBL ttc route created");


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onFinish.finished();
    }

    private List<String> convertToList(String string) {
        List<String> list = new ArrayList<>();
        string = string.replace("{", "").replace("}", "");
        String[] tempArray = string.split(",");
        for (String aTempArray : tempArray) {
            list.add(aTempArray.trim());
        }
        return list;
    }

    private RouteData convertToRoute(String origin, String destination, String type) {
        RouteData data = new RouteData();
        data.setOrigin(origin);
        data.setDestination(destination);
        data.setType(type);
        return data;
    }

    private class TempInfo {
        private List<String> week;
        private List<String> sunday;
        private RouteData data;

        TempInfo(Context context, String origin, String destination, String type, int week, int sunday) {
            data = new RouteData();
            data.setOrigin(origin);
            data.setDestination(destination);
            data.setType(type);
            this.week = convertToList(context.getString(week));
            this.sunday = convertToList(context.getString(sunday));
        }

        List<String> getWeek() {
            return week;
        }

        List<String> getSunday() {
            return sunday;
        }

        public RouteData getData() {
            return data;
        }
    }
}
