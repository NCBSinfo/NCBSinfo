package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class Route {
    private String origin;
    private String destination;
    private TransportType type;
    private TripInfo tripInfo;
    private List<TripDay> dayList;

    public Route() {
        dayList = new ArrayList<>();
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


    public void addDay(int day, List<String> tripList) {
        dayList.add(new TripDay(day, tripList));
        this.tripInfo = new TripInfo(dayList);
    }

    public String nextTrip(Calendar calendar) {
        return tripInfo.nextTrip(calendar).getRawTrip();
    }
}
