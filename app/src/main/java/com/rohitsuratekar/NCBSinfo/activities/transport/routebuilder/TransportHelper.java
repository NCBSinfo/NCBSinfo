package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
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
    public static final String DEFAULT_TRIPS = "{\"00:00\"}";

    /**
     * Basic route converter
     *
     * @param routeNo : Takes predefined route no
     * @return : Routes
     * Default is NCBS-IISC
     */
    public Routes getRoute(int routeNo) {
        for (Routes routes : Routes.values()) {
            if (routes.getRouteNo() == routeNo) {
                return routes;
            }
        }
        return Routes.NCBS_IISC;
    }

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
        for (int i = 0; i < 5; i++) { //This function will scan maximum of 60 days ahead
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

        }

        Log.e(TAG, "Unable to get next trip within given period");
        return null;
    }


    /**
     * @param date : target date
     * @return [Days, Hours, Minute, Seconds] left from current time
     */
    public int[] TimeLeftFromNow(Date date) {
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
    public String reformat(String value) {
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

    //Provides Location of given place
    public LatLng getLocation(Context context, String place, Routes.type type) {
        String latitude, longitude;
        if (type.equals(Routes.type.SHUTTLE)) {
            switch (place) {
                case "ncbs":
                    latitude = context.getString(R.string.ncbs_latitude);
                    longitude = context.getString(R.string.ncbs_longitude);
                    break;
                case "iisc":
                    latitude = context.getString(R.string.iisc_latitude);
                    longitude = context.getString(R.string.iisc_longitude);
                    break;
                case "mandara":
                    latitude = context.getString(R.string.mandara_latitude);
                    longitude = context.getString(R.string.mandara_longitude);
                    break;
                case "icts":
                    latitude = context.getString(R.string.icts_latitude);
                    longitude = context.getString(R.string.icts_longitude);
                    break;
                default:
                    latitude = context.getString(R.string.ncbs_latitude);
                    longitude = context.getString(R.string.ncbs_longitude);
                    break;
            }
        } else {
            if (place.equals("ncbs")) {
                latitude = context.getString(R.string.buggy_ncbs_latitude);
                longitude = context.getString(R.string.buggy_ncbs_longitude);
            } else {
                latitude = context.getString(R.string.buggy_mandara_latitude);
                longitude = context.getString(R.string.buggy_mandara_longitude);
            }
        }
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public List<AlarmModel> getAllReminders(Context context) {
        List<AlarmModel> allList = new AlarmData(context).getAll();
        List<AlarmModel> returnList = new ArrayList<>();
        for (AlarmModel alarm : allList) {
            if (alarm.getLevel().equals(AlarmConstants.alarmLevel.TRANSPORT.name())) {
                returnList.add(alarm);
            }
        }
        return returnList;
    }

    public Routes changeRoute(Routes currentRoute, boolean isNext) {
        if (isNext) {
            if (currentRoute.equals(Routes.NCBS_CBL)) {
                return Routes.NCBS_IISC;
            } else return getRoute(currentRoute.getRouteNo() + 1);
        } else {
            if (currentRoute.equals(Routes.NCBS_IISC)) {
                return Routes.NCBS_CBL;
            } else return getRoute(currentRoute.getRouteNo() - 1);
        }
    }
}
