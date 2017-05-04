package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class DayTrips {
    private int day;
    private List<String> tripsStrings;
    private List<Trip> trips = new ArrayList<>();

    public DayTrips() {
    }

    public int getDay() {
        return day;
    }

    public DayTrips(int day, List<String> tripsStrings) {
        this.day = day;
        this.tripsStrings = tripsStrings;
        updateTrips();

    }

    private void updateTrips() {
        trips.clear();
        for (String s : tripsStrings) {
            trips.add(new Trip(s));
        }
        Calendar cal = trips.get(0).getRealTime();
        for (Trip t : trips) {
            t.setIndex(trips.indexOf(t));
            t.checkDay(cal);
        }
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getTripsStrings() {
        return tripsStrings;
    }

    public void setTripsStrings(List<String> tripsStrings) {
        this.tripsStrings = tripsStrings;
        updateTrips();
    }

    public List<Trip> getTrips() {
        return trips;
    }
}
