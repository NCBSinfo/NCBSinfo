package com.rohitsuratekar.NCBSinfo.activities.home;

import com.rohitsuratekar.NCBSinfo.R;
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

    HomeCardModel(RouteData routeData, String[] trip) {
        origin = routeData.getOrigin();
        destination = routeData.getDestination();
        type = routeData.getType();
        nextTrip = trip[0];
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
        switch (destination) {
            case "ncbs":
                return R.drawable.home_ncbs_image;
            case "iisc":
                return R.drawable.home_iisc_image;
            default:
                return R.drawable.home_ncbs_image;
        }
    }

    public int getColor() {
        switch (type.trim()) {
            case "shuttle":
                return R.color.teal;
            case "buggy":
                return R.color.lime;
            default:
                return R.color.blue;
        }
    }
}
