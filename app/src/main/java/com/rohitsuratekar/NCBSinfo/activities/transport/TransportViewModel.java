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

import java.util.List;

/**
 * Created by Rohit Suratekar on 07-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */
// Keep constructor public
public class TransportViewModel extends ViewModel {

    private MutableLiveData<TransportDetails> currentDetails = new MutableLiveData<>();
    private MutableLiveData<List<RouteData>> allRoutes = new MutableLiveData<>();
    private MutableLiveData<String> showError = new MutableLiveData<>();

    void loadRoute(final Context context, int routeNo) {
        new LoadTransport(context, routeNo, new OnFinishLoading() {
            @Override
            public void onFinish(TransportDetails model) {
                currentDetails.postValue(model);
            }

            @Override
            public void onError() {
                showError.postValue("No default");
            }
        }).execute();

        new GetAllRoutes(context, new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                allRoutes.postValue(routeDataList);
            }
        }).execute();
    }

    MutableLiveData<String> getShowError() {
        return showError;
    }

    MutableLiveData<TransportDetails> getCurrentDetails() {
        return currentDetails;
    }

    MutableLiveData<List<RouteData>> getAllRoutes() {
        return allRoutes;
    }

    interface OnFinishLoading {
        void onFinish(TransportDetails model);

        void onError();
    }

    private static class LoadTransport extends AsyncTask<Object, Void, Void> {

        private OnFinishLoading onFinishLoading;
        private AppData db;
        private int routeNo;
        private TransportDetails model;

        LoadTransport(Context context, int routeNo, OnFinishLoading onFinishLoading) {
            this.onFinishLoading = onFinishLoading;
            this.db = AppData.getDatabase(context);
            this.routeNo = routeNo;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            RouteData routeData = db.routes().getRoute(routeNo);
            if (routeData != null) {
                List<TripData> tripData = db.trips().getTripsByRoute(routeData.getRouteID());
                model = new TransportDetails(routeData, tripData);
                model.setReturnIndex(db.routes().getRouteNo(routeData.getDestination(), routeData.getOrigin(), routeData.getType()));
            } else {
                onFinishLoading.onError();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onFinishLoading.onFinish(model);
        }
    }
}
