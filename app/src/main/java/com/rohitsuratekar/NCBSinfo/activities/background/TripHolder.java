package com.rohitsuratekar.NCBSinfo.activities.background;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.Trips;

import java.util.List;

public class TripHolder {

    private List<Trips> allTrips;

    public List<Trips> getAllTrips() {
        return allTrips;
    }

    public void setAllTrips(List<Trips> allTrips) {
        this.allTrips = allTrips;
    }

    private static final TripHolder holder = new TripHolder();

    public static TripHolder getInstance() {
        return holder;
    }
}
