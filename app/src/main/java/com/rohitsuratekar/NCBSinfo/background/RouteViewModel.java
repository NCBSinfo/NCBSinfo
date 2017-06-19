package com.rohitsuratekar.NCBSinfo.background;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;

import java.util.List;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class RouteViewModel extends AndroidViewModel implements OnDataLoad {

    private MutableLiveData<List<RouteInfo>> allRoutes = new MutableLiveData<>();

    public RouteViewModel(Application application) {
        super(application);
        new LoadData(application, this).execute();

    }

    public MutableLiveData<List<RouteInfo>> getAllRoutes() {
        return allRoutes;
    }


    @Override
    public void loaded(List<RouteInfo> infoList) {
        allRoutes.postValue(infoList);
    }
}
