package com.rohitsuratekar.NCBSinfo.activities.home;

import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class HomeCardModel {
    private String origin;
    private String destination;
    private String type;
    private String nextTrip;
    private int routeID;

    HomeCardModel(RouteData routeData, String[] trip) {
        origin = routeData.getOrigin();
        destination = routeData.getDestination();
        type = routeData.getType();
        nextTrip = trip[0];
        routeID = routeData.getRouteID();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNextTrip() {
        return nextTrip;
    }

    public void setNextTrip(String nextTrip) {
        this.nextTrip = nextTrip;
    }


    public int getImage() {
        return Helper.getImage(destination);
    }

    public int getColor() {
        return Helper.getTypeColor(type);
    }

    public int getRouteID() {
        return routeID;
    }
}
