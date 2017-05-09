package com.rohitsuratekar.NCBSinfo.common;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.secretbiology.helpers.general.Log;

import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class AppState {

    private static AppState instance;
    private List<Route> routeList;

    private AppState(List<Route> allRoutes) {
        this.routeList = allRoutes;
    }

    public static synchronized AppState getInstance() {
        if (instance == null) {
            Log.error("=========================");
            Log.error("AppState is null. Use AppState.initialize() first.");
            Log.error("=========================");
        }
        return instance;
    }

    public static void initialize(List<Route> allRoutes) {
        instance = new AppState(allRoutes);
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }
}
