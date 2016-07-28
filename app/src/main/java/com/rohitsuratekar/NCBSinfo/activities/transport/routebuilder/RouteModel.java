package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 19-07-16.
 */
public class RouteModel {

    int routeNo;
    String from;
    String to;
    String weekDayKey;
    String sundayKey;
    Routes.type type;

    protected RouteModel() {
    }

    public int getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(int routeNo) {
        this.routeNo = routeNo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWeekDayKey() {
        return weekDayKey;
    }

    public void setWeekDayKey(String weekDayKey) {
        this.weekDayKey = weekDayKey;
    }

    public String getSundayKey() {
        return sundayKey;
    }

    public void setSundayKey(String sundayKey) {
        this.sundayKey = sundayKey;
    }

    public Routes.type getType() {
        return type;
    }

    public void setType(Routes.type type) {
        this.type = type;
    }

    public Routes getRoute() {
        return new TransportHelper().getRoute(routeNo);
    }
}
