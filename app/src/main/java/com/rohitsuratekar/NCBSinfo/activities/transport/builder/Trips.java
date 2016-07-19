package com.rohitsuratekar.NCBSinfo.activities.transport.builder;

import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 19-07-16.
 */
public class Trips {

    List<String> monday;
    List<String> tuesday;
    List<String> wednesday;
    List<String> thursday;
    List<String> friday;
    List<String> saturday;
    List<String> sunday;

    List<String> RawWeek;
    List<String> RawSunday;

    public Trips(String[] weekTrips, String[] sundayTrips) {

        List<Calendar> weekCal = new ArrayList<>();
        List<Calendar> sundayCal = new ArrayList<>();

        //Convert to calender list
        for (String s : weekTrips) {
            weekCal.add(new DateConverters().convertToCalendar(s));
            RawWeek.add(s);
        }
        for (String s : sundayTrips) {
            sundayCal.add(new DateConverters().convertToCalendar(s));
            RawSunday.add(s);
        }
        //Start putting values in Monday
        //Put first trip as a first element
        monday.add(new DateConverters().convertToString(weekCal.get(0), DateFormats.TIME_24_HOURS_STANDARD));
        for (Calendar c : weekCal) {
            //Check if time is greater than first trip, if yes add to monday
            if (c.after(weekCal.get(0))) {
                monday.add(new DateConverters().convertToString(c, DateFormats.TIME_24_HOURS_STANDARD));
            }
            //If not, it's value must be after 12.00 AM, hence add it to Tuesday ,
            //These same trips will also be for Sunday
            else {
                tuesday.add(new DateConverters().convertToString(c, DateFormats.TIME_24_HOURS_STANDARD));
                sunday.add(new DateConverters().convertToString(c, DateFormats.TIME_24_HOURS_STANDARD));
            }
        }
        //Now Tuesday will also have all trips we have added to Monday so far
        for (String s : monday) {
            tuesday.add(s);
        }

        //Order Tuesday in chronological order
        tuesday = orderList(tuesday);
        //These Tuesday trips will be same for all days till Saturday
        for (String s : tuesday) {
            wednesday.add(s);
        }
        for (String s : tuesday) {
            thursday.add(s);
        }
        for (String s : tuesday) {
            friday.add(s);
        }
        for (String s : tuesday) {
            saturday.add(s);
        }

        //Start putting values in Sunday, which already have all trips before it's first trip
        sunday.add(new DateConverters().convertToString(sundayCal.get(0), DateFormats.TIME_24_HOURS_STANDARD));
        for (Calendar c : sundayCal) {
            //Check if time is greater than first trip, if yes add to monday
            if (c.after(sundayCal.get(0))) {
                sunday.add(new DateConverters().convertToString(c, DateFormats.TIME_24_HOURS_STANDARD));
            }
            //If not, it's value must be after 12.00 AM, hence add it to Monday
            else {
                //Add at first location
                monday.add(0, new DateConverters().convertToString(c, DateFormats.TIME_24_HOURS_STANDARD));
            }
        }

        //We need to rearrange Monday and Sunday in chronological order
        monday = orderList(monday);
        sunday = orderList(sunday);


    }

    public List<String> get(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return getMonday();
            case Calendar.TUESDAY:
                return getTuesday();
            case Calendar.WEDNESDAY:
                return getWednesday();
            case Calendar.THURSDAY:
                return getThursday();
            case Calendar.FRIDAY:
                return getFriday();
            case Calendar.SATURDAY:
                return getSaturday();
            case Calendar.SUNDAY:
                return getSunday();
        }
        Log.e(getClass().getSimpleName(), "Invalid day for a week");
        return null;
    }

    public List<String> getMonday() {
        return monday;
    }

    public List<String> getTuesday() {
        return tuesday;
    }

    public List<String> getWednesday() {
        return wednesday;
    }

    public List<String> getThursday() {
        return thursday;
    }

    public List<String> getFriday() {
        return friday;
    }

    public List<String> getSaturday() {
        return saturday;
    }

    public List<String> getSunday() {
        return sunday;
    }

    public List<String> getRawWeek() {
        return RawWeek;
    }

    public List<String> getRawSunday() {
        return RawSunday;
    }

    //Orders in chronological order
    private List<String> orderList(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Date leftDate = new DateConverters().convertToDate(lhs);
                Date rightDate = new DateConverters().convertToDate(rhs);
                return leftDate.compareTo(rightDate);
            }
        });
        return list;
    }
}
