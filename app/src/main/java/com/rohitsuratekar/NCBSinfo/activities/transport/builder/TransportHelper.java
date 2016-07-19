package com.rohitsuratekar.NCBSinfo.activities.transport.builder;

import android.util.Log;

import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 19-07-16.
 */
public class TransportHelper {

    private final String TAG = getClass().getSimpleName();

    /**
     * Important assumption in this function is Trip Model will have trip timings in chronological order.
     * Implement trip class strictly in chronological order.
     *
     * @param trips : Trips model containing all trips in each day
     * @return : Date with next trip
     */
    protected Date getNext(Trips trips) {
        //Search will start from current time
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 60; i++) { //This function will scan maximum of 60 days ahead
            //Get today's trips
            List<String> todaysTrips = trips.get(calendar.get(Calendar.DAY_OF_WEEK));
            //Loop over all trips
            for (String trip : todaysTrips) {
                //Convert each trip to calendar
                Calendar newCal = new DateConverters().convertToCalendar(trip);
                //Add date to calender. This will automatically make trips for next day
                newCal.add(Calendar.DATE, i);
                //If calender is after current period, return it
                if (newCal.after(calendar)) {
                    return newCal.getTime();
                }
            }
            //If could't find date, add next day to calendar and
            // reset it to 0 so that it will look for first transport tomorrow
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        Log.e(TAG, "Unable to get next trip within given period");
        return null;
    }


    /**
     * @param date : target date
     * @return [Days, Hours, Minute, Seconds] left from current time
     */
    protected int[] TimeLeftFromNow(Date date) {
        DateTime timestamp = new DateTime(date);
        DateTime currentTime = new DateTime(new Date());
        Interval interval = new Interval(currentTime, timestamp);
        int Seconds = interval.toPeriod().getSeconds();
        int Minute = interval.toPeriod().getMinutes();
        int Hours = interval.toPeriod().getHours();
        int Days = interval.toPeriod().getDays();
        return new int[]{Days, Hours, Minute, Seconds};
    }

    /**
     * Removes all spaces, wrongly formatted date and convert to proper "HH:mm" format
     */
    protected String reformat(String value) {
        //Remove all whitespaces
        value = value.replaceAll("\\s+", "");
        //Check if there are any periods
        if (value.contains(".")) {
            value = value.replace(".", ":");
        }
        //Check if both timings are in hh:mm format
        String[] split = value.split(":");
        //If only one letter, add zero before
        if (split[0].length() == 1) {
            value = "0" + value;
        }
        //If only one letter, add zero after
        if (split[1].length() == 1) {
            value = value + "0";
        }
        //Invalid date
        if (split[0].length() > 2 || split[1].length() > 2) {
            Log.e(TAG, "Invalid time found in reFormat function");
            return "00:00";
        }
        return value;
    }
}
