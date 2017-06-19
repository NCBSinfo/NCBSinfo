package com.rohitsuratekar.NCBSinfo.database;

import java.util.List;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class RouteTrips {
    private Route route;
    private List<Trips> trips;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Trips> getTrips() {
        return trips;
    }

    public void setTrips(List<Trips> trips) {
        this.trips = trips;
    }
}
