package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 20-07-16.
 */
public class TransportDynamics {

    Trips trips;
    Date nextDate;
    int[] timeLeft;

    public TransportDynamics(Trips trips) {
        this.trips = trips;
        this.nextDate = new TransportHelper().getNext(trips);
        this.timeLeft = new TransportHelper().TimeLeftFromNow(nextDate);

    }

    public Date getNextTripDate() {
        return nextDate;
    }

    public int getDaysToNextTrip() {
        return timeLeft[0];
    }

    public int getHoursToNextTrip() {
        return timeLeft[1];
    }

    public int getMinsToNextTrip() {
        return timeLeft[2];
    }

    public int getSecsToNextTrip() {
        return timeLeft[3];
    }
    public String getNextTripString() {
        return new DateConverters().convertToString(nextDate, DateFormats.TIME_24_HOURS_STANDARD);
    }
}
