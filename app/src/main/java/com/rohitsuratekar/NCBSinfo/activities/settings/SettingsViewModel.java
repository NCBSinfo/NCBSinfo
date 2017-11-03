package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.rohitsuratekar.NCBSinfo.background.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 03-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */
// Keep constructor public
public class SettingsViewModel extends ViewModel {

    private MutableLiveData<List<RouteData>> allRoutes = new MutableLiveData<>();

    void loadRoute(final Context context) {
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

    MutableLiveData<List<RouteData>> getAllRoutes() {
        return allRoutes;
    }
}
