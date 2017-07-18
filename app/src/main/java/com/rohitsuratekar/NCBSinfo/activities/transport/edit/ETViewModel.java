package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.background.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private MutableLiveData<ETDataHolder> data = new MutableLiveData<>();
    private MutableLiveData<List<RouteData>> routeList = new MutableLiveData<>();

    public ETViewModel(Application application) {
        super(application);
        data.postValue(new ETDataHolder());
        currentStep.postValue(0);
        new GetAllRoutes(application.getApplicationContext(), new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                routeList.postValue(routeDataList);
            }
        }).execute();
    }

    void editActivityTask(Context context, String[] details) {
        new PrepareETData(new OnDataFinish() {
            @Override
            public void finished(ETDataHolder holder) {
                data.postValue(holder);
                currentStep.postValue(0);
            }
        }, context).execute(details);
    }


    MutableLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    MutableLiveData<ETDataHolder> getData() {
        return data;
    }

    MutableLiveData<List<RouteData>> getRouteList() {
        return routeList;
    }

    static class PrepareETData extends AsyncTask<Object, Void, Void> {

        private OnDataFinish finish;
        private AppData db;

        PrepareETData(OnDataFinish finish, Context context) {
            this.finish = finish;
            db = AppData.getDatabase(context);
        }


        @Override
        protected Void doInBackground(Object... objects) {
            if (objects[0] != null && objects[1] != null && objects[2] != null) {
                String[] details = new String[]{(String) objects[0], (String) objects[1], (String) objects[2]};
                int routeNo = db.routes().getRouteNo(details[0], details[1], details[2]);
                RouteData routeData = db.routes().getRoute(routeNo);
                List<TripData> tripData = db.trips().getTripsByRoute(routeNo);
                ETDataHolder dataHolder = new ETDataHolder();
                dataHolder.setDestination(routeData.getDestination().toUpperCase());
                dataHolder.setOrigin(routeData.getOrigin().toUpperCase());
                dataHolder.setType(getTransportType(routeData.getType()));
                dataHolder.setTripData(tripData);
                finish.finished(dataHolder);
            }
            return null;
        }
    }

    interface OnDataFinish {
        void finished(ETDataHolder holder);
    }

    private static int getTransportType(String type) {
        switch (type.toLowerCase().trim()) {
            case "shuttle":
                return 0;
            case "ttc":
                return 1;
            case "buggy":
                return 2;
            default:
                return 3;
        }
    }

}
