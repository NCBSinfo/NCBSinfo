package com.rohitsuratekar.NCBSinfo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.common.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.common.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

public class BaseViewModel extends ViewModel {

    static String TAG = "BaseViewModel";

    private MutableLiveData<List<RouteData>> allRoutes = new MutableLiveData<>();

    public void loadApp(Context context) {
        new GetAllRoutes(context, new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                Log.i(TAG, "No Routes Founds. Making defaults routes.");
            }
        }).execute();
    }

    private void makeDefault(Context context) {

    }


}
