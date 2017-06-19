package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.TripWeek;
import com.rohitsuratekar.NCBSinfo.database.Route;
import com.rohitsuratekar.NCBSinfo.database.Trips;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class RouteInfo {
    private Route route;
    private List<Trips> trips;
    private String nextTrip;
    private int hour;
    private int minute;
    private String ampm;
    private int day;

    public RouteInfo(Route route, List<Trips> trips) {
        this.route = route;
        this.trips = trips;
        SparseArray<List<String>> map = new SparseArray<>();
        for (Trips t : trips) {
            map.put(t.getDay(), t.getTrips());
        }
        try {
            String n[] = new TripWeek(map).nextTrip(Calendar.getInstance());
            nextTrip = DateConverter.changeFormat(ConverterMode.DATE_FIRST, n[0], "hh:mm a");
            day = Integer.parseInt(n[1]);
        } catch (ParseException e) {
            nextTrip = "00:00 am";
            day = 0;
            Log.error(e.getMessage());
        }

        String m[] = nextTrip.split(":");
        hour = Integer.valueOf(m[0]);
        String k[] = m[1].split(" ");
        minute = Integer.valueOf(k[0]);
        ampm = k[1].toUpperCase();
    }

    public SparseArray<List<String>> getMap() {
        SparseArray<List<String>> map = new SparseArray<>();
        for (Trips t : trips) {
            map.put(t.getDay(), t.getTrips());
        }
        return map;
    }

    public String getNextTrip() {
        return nextTrip;
    }

    public void setNextTrip(String nextTrip) {
        this.nextTrip = nextTrip;
    }

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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getAmPm() {
        return ampm;
    }

    public int getDay() {
        return day;
    }
}
