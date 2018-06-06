package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.rohitsuratekar.NCBSinfo.common.Helper.convertToList;

/**
 * Created by SecretAdmin on 10/6/2017 for NCBSinfo.
 * All code is released under MIT License.
 */

public class CreateDefaultRoutes extends AsyncTask<Void, Void, Void> {

    private OnFinish onFinish;
    private AppData db;
    private List<TempInfo> infoList = new ArrayList<>();
    private List<String> buggyToNCBS = new ArrayList<>();
    private List<String> buggyToMandara = new ArrayList<>();
    private List<String> ncbsToCBL = new ArrayList<>();
    private List<String> ncbsToICTS_1 = new ArrayList<>();
    private List<String> ncbsToICTS_2 = new ArrayList<>();
    private List<String> ncbsToICTS_sunday = new ArrayList<>();
    private boolean fav = true;
    private AppPrefs prefs;
    private boolean reportCrash = false;


    public CreateDefaultRoutes(Context context, OnFinish onFinish) {
        this.onFinish = onFinish;
        this.db = AppData.getDatabase(context);
        this.prefs = new AppPrefs(context);
        infoList.add(new TempInfo(context, "ncbs", "iisc", "shuttle", R.string.def_ncbs_iisc_week, R.string.def_ncbs_iisc_sunday));
        infoList.add(new TempInfo(context, "iisc", "ncbs", "shuttle", R.string.def_iisc_ncbs_week, R.string.def_iisc_ncbs_sunday));
        infoList.add(new TempInfo(context, "ncbs", "mandara", "shuttle", R.string.def_ncbs_mandara_week, R.string.def_ncbs_mandara_sunday));
        infoList.add(new TempInfo(context, "mandara", "ncbs", "shuttle", R.string.def_mandara_ncbs_week, R.string.def_mandara_ncbs_sunday));
        infoList.add(new TempInfo(context, "icts", "ncbs", "shuttle", R.string.def_icts_ncbs_week, R.string.def_icts_ncbs_sunday));

        ncbsToICTS_1 = convertToList(context.getString(R.string.def_ncbs_icts_week_1));
        ncbsToICTS_2 = convertToList(context.getString(R.string.def_ncbs_icts_week_2));
        ncbsToICTS_sunday = convertToList(context.getString(R.string.def_ncbs_icts_sunday));
        buggyToNCBS = convertToList(context.getString(R.string.def_buggy_from_mandara));
        buggyToMandara = convertToList(context.getString(R.string.def_buggy_from_ncbs));
        ncbsToCBL = convertToList(context.getString(R.string.def_ncbs_cbl));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (TempInfo info : infoList) {
            if (fav) {
                info.getData().setFavorite("yes");
                fav = false;
            } else {
                info.getData().setFavorite("no");
            }

            int testID = db.routes().getRouteNo(info.getData().getOrigin(), info.getData().getDestination(), info.getData().getType());

            if (testID == 0) {
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
                Log.i(getClass().getSimpleName(), "Created route for " + info.getData().getOrigin() + "-" + info.getData().getDestination());
            } else {
                reportCrash = true;
            }
        }

        //NCBS-ICTS Shuttle
        int testID5 = db.routes().getRouteNo("ncbs", "icts", "shuttle");
        if (testID5 == 0) {
            RouteData r4 = convertToRoute("ncbs", "icts", "shuttle");
            int r4_id = (int) db.routes().insertRoute(r4);
            for (int day = Calendar.SUNDAY; day < Calendar.SATURDAY + 1; day++) {
                TripData t4 = new TripData();
                t4.setRouteID(r4_id);
                t4.setDay(day);
                switch (day) {
                    case Calendar.SUNDAY:
                        t4.setTrips(ncbsToICTS_sunday);
                        break;
                    case Calendar.MONDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                        t4.setTrips(ncbsToICTS_1);
                        break;
                    default:
                        t4.setTrips(ncbsToICTS_2);
                }
                db.trips().insertTrips(t4);
            }

        } else {
            reportCrash = true;
        }

        Log.i(getClass().getSimpleName(), "NCBS-ICTS week shuttle route created");

        int testID2 = db.routes().getRouteNo("ncbs", "mandara", "buggy");
        if (testID2 == 0) {
            RouteData r1 = convertToRoute("ncbs", "mandara", "buggy");
            db.routes().insertRoute(r1);
            int r1_id = db.routes().getRouteNo(r1.getOrigin(), r1.getDestination(), r1.getType());
            TripData t1 = new TripData();
            t1.setRouteID(r1_id);
            t1.setTrips(buggyToMandara);
            t1.setDay(Calendar.MONDAY);
            db.trips().insertTrips(t1);
        } else {
            reportCrash = true;
        }

        int testID3 = db.routes().getRouteNo("mandara", "ncbs", "buggy");
        if (testID3 == 0) {
            RouteData r2 = convertToRoute("mandara", "ncbs", "buggy");
            db.routes().insertRoute(r2);
            int r2_id = db.routes().getRouteNo(r2.getOrigin(), r2.getDestination(), r2.getType());
            TripData t2 = new TripData();
            t2.setRouteID(r2_id);
            t2.setTrips(buggyToNCBS);
            t2.setDay(Calendar.MONDAY);
            db.trips().insertTrips(t2);

        } else {
            reportCrash = true;
        }

        Log.i(getClass().getSimpleName(), "Buggy route created");

        int testID4 = db.routes().getRouteNo("ncbs", "cbl", "ttc");
        if (testID4 == 0) {
            RouteData r3 = convertToRoute("ncbs", "cbl", "ttc");
            db.routes().insertRoute(r3);
            int r3_id = db.routes().getRouteNo(r3.getOrigin(), r3.getDestination(), r3.getType());
            TripData t3 = new TripData();
            t3.setRouteID(r3_id);
            t3.setTrips(ncbsToCBL);
            t3.setDay(Calendar.MONDAY);
            db.trips().insertTrips(t3);
        } else {
            reportCrash = true;
        }

        Log.i(getClass().getSimpleName(), "NCBS-CBL ttc route created");


        int favRoute = db.routes().getRouteNo(prefs.getFavoriteOrigin(), prefs.getFavoriteDestination(), prefs.getFavoriteType());

        if (favRoute != 0) {
            db.routes().removeAllFavorite();
            db.routes().setFavorite(favRoute);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onFinish.finished();
        if (reportCrash) {
            Crashlytics.log("Problem in creating default routes.");
        }
    }


    private RouteData convertToRoute(String origin, String destination, String type) {
        RouteData data = new RouteData();
        data.setOrigin(origin);
        data.setDestination(destination);
        data.setType(type);
        data.setFavorite("no");
        data.setAuthor("NCBSinfo");
        data.setCreatedOn("2017-07-21 00:00:00");
        data.setModifiedOn(Helper.timestamp());
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
            data.setAuthor("NCBSinfo");
            data.setCreatedOn("2017-07-21 00:00:00");
            data.setModifiedOn(Helper.timestamp());
            data.setFavorite("no");
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