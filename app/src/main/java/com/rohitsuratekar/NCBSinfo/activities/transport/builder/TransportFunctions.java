package com.rohitsuratekar.NCBSinfo.activities.transport.builder;

import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 19-07-16.
 */
public interface TransportFunctions {

    Date getNextTrip();

    int getDaysToNext();

    int getHoursToNext();

    int getMinutesToNext();

    int getSecondsToNext();

}
