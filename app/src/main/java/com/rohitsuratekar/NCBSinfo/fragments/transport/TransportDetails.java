package com.rohitsuratekar.NCBSinfo.fragments.transport;

import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 07-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportDetails {

    private RouteData routeData;
    private String origin;
    private String destination;
    private String type;
    private int routeID;
    private boolean isReturnAvailable;
    private int returnIndex;
    private NextTrip next;
    private String[] originalTrip;
    private int originalDay;


    public TransportDetails(RouteData routeData, List<TripData> tripData) {
        this.routeData = routeData;
        origin = routeData.getOrigin();
        destination = routeData.getDestination();
        routeID = routeData.getRouteID();
        type = routeData.getType();
        next = new NextTrip(tripData);
    }

    public RouteData getRouteData() {
        return routeData;
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

    public String[] getOriginalTrip() {
        return originalTrip;
    }

    public void setOriginalTrip(String[] originalTrip) {
        this.originalTrip = originalTrip;
    }

    public int getOriginalDay() {
        return originalDay;
    }

    public void setOriginalDay(int originalDay) {
        this.originalDay = originalDay;
    }
}
