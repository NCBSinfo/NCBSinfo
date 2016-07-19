package com.rohitsuratekar.NCBSinfo.activities.transport.builder;

/**
 * Base transport interface
 * Every transport should have these properties
 */
public interface Transport {

    String getOrigin();

    String getDestination();

    String getType();

    int getRouteNo();

    Trips getTrips();


}
