package com.rohitsuratekar.NCBSinfo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rohitsuratekar.NCBSinfo.common.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.common.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.common.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseViewModel extends ViewModel {

    static String TAG = "BaseViewModel";

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

        LoadTransport(Context context, OnFinishLoading onFinishLoading) {
            this.onFinishLoading = onFinishLoading;
            this.db = AppData.getDatabase(context);
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
            return null;
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
