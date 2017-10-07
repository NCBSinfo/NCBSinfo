package com.rohitsuratekar.NCBSinfo.activities.transport;

import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 07-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportDetails {

    private RouteData routeData;
    private List<TripData> tripData;
    private String origin;
    private String destination;
    private String type;
    private int routeID;
    private boolean isReturnAvailable;
    private int returnIndex;
    private NextTrip next;


    TransportDetails(RouteData routeData, List<TripData> tripData) {
        this.routeData = routeData;
        this.tripData = tripData;
        origin = routeData.getOrigin();
        destination = routeData.getDestination();
        routeID = routeData.getRouteID();
        type = routeData.getType();
        next = new NextTrip(tripData);
    }


    public void setReturnAvailable(boolean returnAvailable) {
        isReturnAvailable = returnAvailable;
    }

    public void setReturnIndex(int returnIndex) {
        this.isReturnAvailable = (returnIndex != 0);
        this.returnIndex = returnIndex;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getType() {
        return type;
    }

    public boolean isReturnAvailable() {
        return isReturnAvailable;
    }

    public int getReturnIndex() {
        return returnIndex;
    }

    public List<String> getTrips(Calendar calendar) {
        return next.getTrips(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public String[] getNextTripDetails(Calendar calendar) throws ParseException {
        return next.calculate(calendar);
    }

    public int getRouteID() {
        return routeID;
    }
}
