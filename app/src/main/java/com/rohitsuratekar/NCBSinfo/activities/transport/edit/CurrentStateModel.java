package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.List;

public class CurrentStateModel {

    private String origin;
    private String destination;
    private List<RouteModel> routeList;

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

    public List<RouteModel> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<RouteModel> routeList) {
        this.routeList = routeList;
    }
}
