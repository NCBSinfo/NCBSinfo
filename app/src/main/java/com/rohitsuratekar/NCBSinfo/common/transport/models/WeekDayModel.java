package com.rohitsuratekar.NCBSinfo.common.transport.models;

import java.util.List;

public class WeekDayModel {

    List<String> trips;

    public WeekDayModel(List<String> trips) {
        this.trips = trips;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }
}
