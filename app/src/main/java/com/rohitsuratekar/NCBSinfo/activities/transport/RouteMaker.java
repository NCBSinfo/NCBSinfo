package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.database.RouteManager;
import com.rohitsuratekar.NCBSinfo.database.RouteModel;
import com.secretbiology.helpers.general.General;

import java.util.List;

public class RouteMaker {

    private static final String DEFAULT_TRIGGER = "none";
    private Context context;
    private RouteManager manager;

    public RouteMaker(Context context) {
        this.context = context;
        this.manager = new RouteManager(context);
    }

    public RouteModel make(String origin, String destination, int day, List<String> trips, String type) {
        RouteModel route = new RouteModel();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDay(day);
        route.setTrips(trips);
        route.setCreatedOn(General.timeStamp());
        route.setAuthor("Default");
        route.setType(type);
        int temp = manager.existsRoute(origin, destination, type);
        if (temp == -1) {
            route.setRoute(manager.getNextRoute(origin, destination));
        } else {
            route.setRoute(temp);
        }
        route.setTrigger(DEFAULT_TRIGGER);
        route.setNotes("");
        return route;

    }

}
