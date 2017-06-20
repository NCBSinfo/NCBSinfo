package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.background.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportViewModel extends ViewModel implements OnFinishLoading {

    private MutableLiveData<TransportModel> currentModel = new MutableLiveData<>();
    private MutableLiveData<Object[]> returnTrip = new MutableLiveData<>();
    private MutableLiveData<List<RouteData>> routeList = new MutableLiveData<>();


    void loadRoute(Context context, int routeNo, Calendar calendar) {
        new LoadTransport(context, routeNo, this).execute(context, calendar);
    }

    MutableLiveData<TransportModel> getCurrentModel() {
        return currentModel;
    }

    MutableLiveData<Object[]> getReturnTrip() {
        return returnTrip;
    }

    MutableLiveData<List<RouteData>> getRouteList() {
        return routeList;
    }

    @Override
    public void onFinish(TransportModel model) {
        currentModel.postValue(model);
    }

    @Override
    public void onCheckingReverse(boolean isAvailable, int index) {
        returnTrip.postValue(new Object[]{isAvailable, index});
    }

    @Override
    public void onError(String message) {

    }

    void retrieveRouteList(Context context) {
        new GetAllRoutes(context, new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                routeList.postValue(routeDataList);
            }
        }).execute();
    }

    private static class LoadTransport extends AsyncTask<Object, Void, Void> {

        private OnFinishLoading onFinishLoading;
        private AppData db;
        private int routeNo;
        private TransportModel model;
        private boolean returnAvailable;
        private int returnIndex;

        LoadTransport(Context context, int routeNo, OnFinishLoading onFinishLoading) {
            this.onFinishLoading = onFinishLoading;
            this.db = AppData.getDatabase(context);
            this.routeNo = routeNo;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            Calendar calendar = (Calendar) objects[1];
            RouteData routeData = db.routes().getRoute(routeNo);
            if (routeData != null) {
                List<TripData> tripData = db.trips().getTripsByRoute(routeData.getRouteID());
                model = new TransportModel(context, calendar, routeData, tripData);
                returnIndex = db.routes().getRouteNo(routeData.getDestination(), routeData.getOrigin(), routeData.getType());
                returnAvailable = returnIndex != 0;
            } else {
                onFinishLoading.onError("Unable to retrieve data with given route");
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onFinishLoading.onFinish(model);
            onFinishLoading.onCheckingReverse(returnAvailable, returnIndex);
        }
    }
}
