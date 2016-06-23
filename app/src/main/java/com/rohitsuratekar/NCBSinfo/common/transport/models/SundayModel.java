package com.rohitsuratekar.NCBSinfo.common.transport.models;

import java.util.List;

public class SundayModel {

    List<String> trips;

    public SundayModel(List<String> trips) {
        this.trips = trips;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }
}
