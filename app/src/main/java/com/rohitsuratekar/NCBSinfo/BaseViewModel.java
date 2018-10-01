package com.rohitsuratekar.NCBSinfo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.common.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.common.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.rohitsuratekar.NCBSinfo.common.Helper.convertToList;

public class BaseViewModel extends ViewModel {

    private static String TAG = "BaseViewModel";

    private MutableLiveData<List<TransportDetails>> allTransport = new MutableLiveData<>();

    public MutableLiveData<List<TransportDetails>> getAllTransport() {
        return allTransport;
    }

    public void loadApp(final Context context) {
        new GetAllRoutes(context, new OnFinish() {
            @Override
            public void finished() {
                // Will be empty
            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                if (routeDataList.size() == 0) {
                    Log.i(TAG, "No Routes Founds. Making defaults routes.");
                    makeDefault(context);
                } else {
                    loadRoutes(context);
                }
            }
        }).execute();
    }

    private void makeDefault(final Context context) {

        new CreateDefaultRoutes(context, new OnFinish() {
            @Override
            public void finished() {
                loadRoutes(context);
            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                //Will be empty
                //
            }
        }).execute();

    }

    private void loadRoutes(Context context) {
        Log.i(TAG, "Found routes, collecting trip information");
        new LoadTransport(context, new OnFinishLoading() {
            @Override
            public void onFinish(List<TransportDetails> transportDetailsList) {
                allTransport.postValue(transportDetailsList);
            }
        }).execute();
    }

    private static class LoadTransport extends AsyncTask<Object, Void, Void> {

        private OnFinishLoading onFinishLoading;
        private AppData db;
        private List<TransportDetails> modelList;
        private AppPrefs prefs;
        private int versionCode;
        private List<String> buggyToNCBS;
        private List<String> buggyToMandara;
        private List<String> ncbsToCBL;

        LoadTransport(Context context, OnFinishLoading onFinishLoading) {
            this.onFinishLoading = onFinishLoading;
            this.db = AppData.getDatabase(context);
            this.prefs = new AppPrefs(context);
            PackageInfo packageInfo = null;
            try {
                packageInfo = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0);
                this.versionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to get package version");
                this.versionCode = 0;
            }
            buggyToNCBS = convertToList(context.getString(R.string.def_buggy_from_mandara));
            buggyToMandara = convertToList(context.getString(R.string.def_buggy_from_ncbs));
            ncbsToCBL = convertToList(context.getString(R.string.def_ncbs_cbl));

        }

        @Override
        protected Void doInBackground(Object... objects) {

            List<RouteData> routeDataList = db.routes().getRouteNames();
            modelList = new ArrayList<>();
            int favRoute = -1;

            for (RouteData r : routeDataList) {
                RouteData routeData = db.routes().getRoute(r.getRouteID());
                if (routeData != null) {
                    List<TripData> tripData = db.trips().getTripsByRoute(routeData.getRouteID());
                    TransportDetails model = new TransportDetails(routeData, tripData);
                    model.setReturnIndex(db.routes().getRouteNo(routeData.getDestination(), routeData.getOrigin(), routeData.getType()));
                    modelList.add(model);
                    if (routeData.getFavorite().toLowerCase().equals("yes")) {
                        favRoute = modelList.indexOf(model);
                    }
                } else {
                    Log.e(TAG, "Something went wrong while retrieving routes");
                    Crashlytics.log("Something went wrong while retrieving routes");
                }
            }

            if (favRoute == -1 && modelList.size() > 0) {
                Log.i(TAG, "No favorite route found, making some random favorite route");
                db.routes().removeAllFavorite();
                db.routes().setFavorite(modelList.get(0).getRouteID());
            } else {
                Collections.swap(modelList, 0, favRoute);
            }
            checkUpgrade();
            return null;
        }

        void checkUpgrade() {
            Log.i(TAG, "Checking if new data-set upgrade is available.");
            if (versionCode > 62 && !prefs.getUpdate_62_63()) {
                Log.i(TAG, "Updating buggy and TTC w.r.t. 2 Oct 2018");

                SimpleDateFormat f2 = new SimpleDateFormat(Helper.FORMAT_TIMESTAMP, Locale.ENGLISH);
                String modifiedDate = f2.format(new Date());
                int r1 = db.routes().getRouteNo("ncbs", "mandara", "buggy");
                int r2 = db.routes().getRouteNo("mandara", "ncbs", "buggy");
                int r3 = db.routes().getRouteNo("ncbs", "cbl", "ttc");

                if (r1 != 0) {
                    List<TripData> t1 = db.trips().getTripsByRoute(r1);
                    for (TripData t : t1) {
                        t.setTrips(buggyToMandara);
                        db.trips().updateTrips(t);
                    }

                    RouteData rd1 = db.routes().getRoute(r1);
                    rd1.setModifiedOn(modifiedDate);
                    db.routes().updateRoute(rd1);

                }
                if (r2 != 0) {
                    List<TripData> t2 = db.trips().getTripsByRoute(r2);
                    for (TripData t : t2) {
                        t.setTrips(buggyToNCBS);
                        db.trips().updateTrips(t);
                    }

                    RouteData rd2 = db.routes().getRoute(r2);
                    rd2.setModifiedOn(modifiedDate);
                    db.routes().updateRoute(rd2);
                }
                if (r3 != 0) {
                    List<TripData> t3 = db.trips().getTripsByRoute(r3);
                    for (TripData t : t3) {
                        t.setTrips(ncbsToCBL);
                        db.trips().updateTrips(t);
                    }

                    RouteData rd3 = db.routes().getRoute(r3);
                    rd3.setModifiedOn(modifiedDate);
                    db.routes().updateRoute(rd3);
                }
                prefs.updated_62_63();
            }

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onFinishLoading.onFinish(modelList);
        }
    }

    interface OnFinishLoading {
        void onFinish(List<TransportDetails> transportDetailsList);
    }


}
