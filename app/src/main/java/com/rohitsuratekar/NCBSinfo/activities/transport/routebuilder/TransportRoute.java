package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;

import java.util.Date;

/**
 * All basic functions needed for any transport Route
 */
public abstract class TransportRoute {

    public abstract Routes getRoute();

    public abstract String getOrigin();

    public abstract String getDestination();

    public abstract int getRouteNo();

    public abstract Routes.type getRouteType();

    public abstract String getType();

    public abstract Trips getTrips();

    public abstract Date getNextTripDate();

    public abstract String getNextTripString();

    public abstract String getWeekTitle();

    public abstract String getSundayTitle();

    public abstract String getFooter1();

    public abstract String getFooter2();

    public abstract LatLng getOriginLocation();

    public abstract LatLng getDestinationLocation();

    public abstract int getDaysToNextTrip();

    public abstract int getHoursToNextTrip();

    public abstract int getMinsToNextTrip();

    public abstract int getSecsToNextTrip();

}
