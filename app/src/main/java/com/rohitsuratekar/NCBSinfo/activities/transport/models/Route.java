package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Route {
    private List<RouteModel> allRoutes;
    private String origin;
    private String destination;
    private TransportType type;
    private int routeNo;
    private SparseArray<List<String>> map = new SparseArray<>();
    private List<String> defaultList = new ArrayList<>();

    public Route(@NonNull List<RouteModel> allRoutes) {
        this.allRoutes = allRoutes;
        this.origin = allRoutes.get(0).getOrigin();
        this.destination = allRoutes.get(0).getDestination();
        this.routeNo = allRoutes.get(0).getRoute();
        this.type = allRoutes.get(0).getType();
        boolean gotItem = true;
        for (RouteModel r : allRoutes) {
            this.map.put(r.getDay(), r.getTrips());
            if (r.getDay() != Calendar.SUNDAY && gotItem) {
                this.defaultList = r.getTrips();
                gotItem = false;
            }
        }
        if (gotItem) {
            this.defaultList = allRoutes.get(0).getTrips();
        }
    }

    public boolean isRegular() {
        return allRoutes.size() == 2 && map.get(Calendar.SUNDAY) != null;
    }

    public List<String> getDefaultList() {
        return defaultList;
    }

    public SparseArray<List<String>> getMap() {
        return map;
    }

    public void setMap(SparseArray<List<String>> map) {
        this.map = map;
    }

    public List<RouteModel> getAllRoutes() {
        return allRoutes;
    }

    public void setAllRoutes(List<RouteModel> allRoutes) {
        this.allRoutes = allRoutes;
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

    public int getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(int routeNo) {
        this.routeNo = routeNo;
    }
}
