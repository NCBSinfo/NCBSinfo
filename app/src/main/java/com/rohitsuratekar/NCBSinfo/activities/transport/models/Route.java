package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportMethods;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class Route {

    private String origin;
    private String destination;
    private TransportType type;
    private int color;
    private int colorDark;
    private List<DayTrips> dayTrips;
    private SparseArray<List<Trip>> weekMap;
    private TransportUI ui;

    public Route() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColorDark() {
        return colorDark;
    }

    public void setColorDark(int colorDark) {
        this.colorDark = colorDark;
    }

    public void setDayTrips(List<DayTrips> dayTrips) {
        this.dayTrips = dayTrips;
        this.weekMap = TransportMethods.getWeekMap(dayTrips);
        this.ui = new TransportUI(dayTrips);
    }

    public TransportUI getUi() {
        return ui;
    }

    public String nextTrip(Calendar calendar) {
        return TransportMethods.nextTrip(weekMap, calendar).getFormatted();
    }
}
