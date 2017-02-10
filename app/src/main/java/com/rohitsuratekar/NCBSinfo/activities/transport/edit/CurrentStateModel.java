package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import java.util.List;

public class CurrentStateModel {

    private String origin;
    private String destination;
    private List<String> routeList;
    private int day;
    private String firstTrip;

    public CurrentStateModel() {
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

    public List<String> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<String> routeList) {
        this.routeList = routeList;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getFirstTrip() {
        return firstTrip;
    }

    public void setFirstTrip(String firstTrip) {
        this.firstTrip = firstTrip;
    }
}
