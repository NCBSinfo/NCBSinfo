package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/transport/TransportHelper.java
import com.rohitsuratekar.NCBSinfo.activities.transport.models.MondayModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.SundayModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.WeekDayModel;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import org.joda.time.DateTime;
import org.joda.time.Interval;
=======
import com.rohitsuratekar.NCBSinfo.common.transport.models.BuggyModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.MondayModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.ShuttleModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.SundayModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.WeekDayModel;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/common/transport/TransportHelper.java

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransportHelper implements AlarmConstants {

    public static final int DEFAULT_NO = 1989;
    public static final String DEFAULT_TRIPS = "{\"00:00\"}";

    Context context;
    SharedPreferences pref;

    public TransportHelper(Context context) {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/transport/TransportHelper.java
    public Routes getRoute(int routeNo) {
        for (Routes currentRoute : Routes.values()) {
            if (currentRoute.getRouteNo() == routeNo) {
                return currentRoute;
            }
=======
    /**
     * @param route : Route number
     * @return String[0] : Where is transport from
     * String[1] : Where is transport going
     * String[2] : SharedPreference key for Sunday Trips
     * String[3] : SharedPreference key for Weekday Trips
     */
    public String[] routeToStrings(int route) {
        String from;
        String to;
        String weekPreferenceKey;
        String sundayPreferenceKey;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        switch (route) {
            case TransportConstants.ROUTE_NCBS_IISC:
                from = "ncbs";
                to = "iisc";
                weekPreferenceKey = TransportConstants.NCBS_IISC_WEEK;
                sundayPreferenceKey = TransportConstants.NCBS_IISC_SUNDAY;
                break;
            case TransportConstants.ROUTE_IISC_NCBS:
                from = "iisc";
                to = "ncbs";
                weekPreferenceKey = TransportConstants.IISC_NCBS_WEEK;
                sundayPreferenceKey = TransportConstants.IISC_NCBS_SUNDAY;
                break;
            case TransportConstants.ROUTE_NCBS_MANDARA:
                from = "ncbs";
                to = "mandara";
                weekPreferenceKey = TransportConstants.NCBS_MANDARA_WEEK;
                sundayPreferenceKey = TransportConstants.NCBS_MANDARA_SUNDAY;
                break;
            case TransportConstants.ROUTE_MANDARA_NCBS:
                from = "mandara";
                to = "ncbs";
                weekPreferenceKey = TransportConstants.MANDARA_NCBS_WEEK;
                sundayPreferenceKey = TransportConstants.MANDARA_NCBS_SUNDAY;
                break;
            case TransportConstants.ROUTE_BUGGY_NCBS:
                from = "ncbs";
                to = "mandara";
                weekPreferenceKey = TransportConstants.BUGGY_NCBS;
                sundayPreferenceKey = TransportConstants.BUGGY_NCBS;
                break;
            case TransportConstants.ROUTE_BUGGY_MANDARA:
                from = "mandara";
                to = "ncbs";
                weekPreferenceKey = TransportConstants.BUGGY_MANDARA;
                sundayPreferenceKey = TransportConstants.BUGGY_MANDARA;
                break;
            case TransportConstants.ROUTE_NCBS_ICTS:
                from = "ncbs";
                to = "icts";
                weekPreferenceKey = TransportConstants.NCBS_ICTS_WEEK;
                sundayPreferenceKey = TransportConstants.NCBS_ICTS_SUNDAY;
                break;
            case TransportConstants.ROUTE_ICTS_NCBS:
                from = "icts";
                to = "ncbs";
                weekPreferenceKey = TransportConstants.ICTS_NCBS_WEEK;
                sundayPreferenceKey = TransportConstants.ICTS_NCBS_SUNDAY;
                break;
            case TransportConstants.ROUTE_NCBS_CBL:
                from = "ncbs";
                to = "cbl";
                weekPreferenceKey = TransportConstants.NCBS_CBL;
                sundayPreferenceKey = TransportConstants.NCBS_CBL;
                break;
            default:
                from = "ncbs";
                to = "iisc";
                weekPreferenceKey = TransportConstants.NCBS_IISC_WEEK;
                sundayPreferenceKey = TransportConstants.NCBS_IISC_SUNDAY;
                break;
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/common/transport/TransportHelper.java
        }
        return Routes.NCBS_IISC;
    }

    /**
     * Takes raw trip input
     * Returns rearranged properly along whole week with
     * returnLists[0] = SundayModel
     * returnLists[1] = MondayModel
     * returnLists[2] = WeekDayModel
     */

    public List<List<String>> rawToRegular(List<String> sundayTrips, List<String> weekdayTrips) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat returnFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        List<Date> sundayRaw = new ArrayList<>();
        List<Date> sundayHalf = new ArrayList<>();
        List<Date> sunday = new ArrayList<>();
        List<Date> sundayFinal = new ArrayList<>();
        List<Date> monday = new ArrayList<>();
        List<Date> weekdayRaw = new ArrayList<>();
        List<Date> weekdayHalf = new ArrayList<>();
        List<Date> weekday = new ArrayList<>();
        List<Date> weekdayFinal = new ArrayList<>();

        //Convert to raw Dates

        for (String value : sundayTrips) {
            value = reformat(value);
            //Essential to keep date fixed while comparing timings
            //It is my birthday :P But it can be any constant date
            value = "10/08/1989 " + value;
            try {
                sundayRaw.add(format.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (String value : weekdayTrips) {
            value = reformat(value);
            //Essential to keep date fixed while comparing timings
            //It is my birthday :P But it can be any constant date
            value = "10/08/1989 " + value;
            try {
                weekdayRaw.add(format.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Sort Date after 12 AM and before first trip
        for (Date date : sundayRaw) {
            if (sunday.size() == 0) {
                sunday.add(date);
            } else {
                if (sunday.get(sunday.size() - 1).compareTo(date) <= 0) {
                    sunday.add(date);
                } else {
                    sundayHalf.add(date);
                }
            }
        }

        for (Date date : weekdayRaw) {
            if (weekday.size() == 0) {
                weekday.add(date);
            } else {
                if (weekday.get(weekday.size() - 1).compareTo(date) <= 0) {
                    weekday.add(date);
                } else {
                    weekdayHalf.add(date);
                }
            }
        }

        //Make Monday
        //First add all values after 12 AM Sunday
        for (Date value : sundayHalf) {
            monday.add(value);
        }
        //Now add regular weekdays before 12 AM
        for (Date value : weekday) {
            monday.add(value);
        }
        //Now add Weekday values after 12 AM
        for (Date value : weekdayHalf) {
            weekdayFinal.add(value);
        }
        //Add Weekdays before 12 AM
        for (Date value : weekday) {
            weekdayFinal.add(value);
        }
        //Add sunday after 12 AM
        for (Date value : weekdayHalf) {
            sundayFinal.add(value);
        }
        //Add rest of sunday
        for (Date value : sunday) {
            sundayFinal.add(value);
        }

        List<List<String>> returnLists = new ArrayList<>();
        List<String> sundayStrings = new ArrayList<>();
        List<String> mondayStrings = new ArrayList<>();
        List<String> weekdayStrigs = new ArrayList<>();

        for (Date date : sundayFinal) {
            sundayStrings.add(reformat(returnFormat.format(date)));
        }
        for (Date date : monday) {
            mondayStrings.add(reformat(returnFormat.format(date)));
        }
        for (Date date : weekdayFinal) {
            weekdayStrigs.add(reformat(returnFormat.format(date)));
        }

        returnLists.add(sundayStrings);
        returnLists.add(mondayStrings);
        returnLists.add(weekdayStrigs);

        return returnLists;
    }

    //Removes all spaces, wrongly formatted date and convert to proper "HH:mm" format
    public String reformat(String value) {
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

        return value;
    }

    /**
     * Takes input as regular trips (not raw) and returns
     * String[0] = Day of next trip
     * String[1] = Time of next trip (in "HH:mm" format)
     */
    public String[] nextTrip(Routes currentRoute) {

        List<List<String>> allModels = new TransportHelper(context)
                .rawToRegular(Arrays.asList(new Converters().stringToarray(pref.getString(currentRoute.getSundayKey(), DEFAULT_TRIPS)))
                        , Arrays.asList(new Converters().stringToarray(pref.getString(currentRoute.getWeekKey(), DEFAULT_TRIPS))));


        SundayModel sunday = new SundayModel(allModels.get(0));
        MondayModel monday = new MondayModel(allModels.get(1));
        WeekDayModel weekday = new WeekDayModel(allModels.get(2));

        Calendar calendar = Calendar.getInstance();
        String targetString;
        int Day;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                int i = getTripNumber(sunday.getTrips());
                if (i != DEFAULT_NO) {
                    targetString = sunday.getTrips().get(i);
                    Day = Calendar.SUNDAY;
                } else {
                    targetString = monday.getTrips().get(0);
                    Day = Calendar.MONDAY;
                }
                break;
            case Calendar.MONDAY:
                i = getTripNumber(monday.getTrips());
                if (i != DEFAULT_NO) {
                    targetString = monday.getTrips().get(i);
                    Day = Calendar.MONDAY;
                } else {
                    targetString = weekday.getTrips().get(0);
                    Day = Calendar.TUESDAY;
                }
                break;
            case Calendar.SATURDAY:
                i = getTripNumber(weekday.getTrips());
                if (i != DEFAULT_NO) {
                    targetString = weekday.getTrips().get(i);
                    Day = Calendar.SATURDAY;
                } else {
                    targetString = sunday.getTrips().get(0);
                    Day = Calendar.SUNDAY;
                }
                break;
            default:
                i = getTripNumber(weekday.getTrips());
                if (i != DEFAULT_NO) {
                    targetString = weekday.getTrips().get(i);
                    Day = calendar.get(Calendar.DAY_OF_WEEK);
                } else {
                    targetString = weekday.getTrips().get(0);
                    Day = calendar.get(Calendar.DAY_OF_WEEK) + 1;
                }
                break;
        }
        return new String[]{String.valueOf(Day), targetString};
    }

    //Returns next trip from given list (based on current time)
    public int getTripNumber(List<String> trips) {
        for (int i = 0; i < trips.size(); i++) {
            Date date = new DateConverters().convertToDate(trips.get(i));
            Date currentDate = new Date();
            if (currentDate.before(date)) {
                return i;
            }
        }
        return DEFAULT_NO;
    }


    //Provides Location of given place
    public LatLng getLocation(Context context, String place, boolean isBuggy) {
        String latitude, longitude;
        if (isBuggy) {
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

<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/transport/TransportHelper.java
=======
    public TransportModel getTransport(Context context, int route) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        TransportModel transport;
        BuggyModel buggy;
        ShuttleModel shuttle;
        SundayModel sunday;
        WeekDayModel weekday;
        boolean isBuggy = false;
        if (route == TransportConstants.ROUTE_BUGGY_NCBS || route == TransportConstants.ROUTE_BUGGY_MANDARA) {
            isBuggy = true;
        }
        if (isBuggy) {
            String from = routeToStrings(route)[0];
            String to = routeToStrings(route)[1];
            buggy = new BuggyModel(
                    new Utilities().stringToarray(pref.getString(routeToStrings(route)[3], context.getString(R.string.def_buggy_from_ncbs))),
                    new Utilities().stringToarray(pref.getString(routeToStrings(route)[2], context.getString(R.string.def_buggy_from_ncbs)))
                    , from, to, context);
            transport = new TransportModel(context, buggy);
        } else {
            weekday = new WeekDayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(routeToStrings(route)[3], context.getString(R.string.def_ncbs_iisc_week)))));
            sunday = new SundayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(routeToStrings(route)[2], context.getString(R.string.def_ncbs_iisc_week)))));
            shuttle = new ShuttleModel(route, sunday, weekday, context);
            transport = new TransportModel(context, shuttle);
        }
        return transport;
    }

    //Function will return time left [Seconds, Minutes, Hours, Days]
    public float[] TimeLeft(String currentTime, String DestinationTime) {

        float[] timeLeft = new float[4];
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(currentTime);
            d2 = format.parse(DestinationTime);
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/common/transport/TransportHelper.java

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

    public List<AlarmModel> getAllReminders() {
        List<AlarmModel> allList = new AlarmData(context).getAll();
        List<AlarmModel> returnList = new ArrayList<>();
        for (AlarmModel alarm : allList) {
            if (alarm.getLevel().equals(alarmLevel.TRANSPORT.name())) {
                returnList.add(alarm);
            }
        }
        return returnList;
    }
<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/transport/TransportHelper.java

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
=======
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/common/transport/TransportHelper.java
}



