package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.MondayModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.SundayModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.WeekDayModel;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

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

    public Routes getRoute(int routeNo) {
        for (Routes currentRoute : Routes.values()) {
            if (currentRoute.getRouteNo() == routeNo) {
                return currentRoute;
            }
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
        SimpleDateFormat onlyDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        SimpleDateFormat finalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        Date currentDate = new Date();
        int tripNumber = DEFAULT_NO;
        for (int i = 0; i < trips.size(); i++) {
            try {
                String dateString = onlyDate.format(currentDate) + " " + trips.get(i);
                Date date = finalFormat.parse(dateString);
                if (currentDate.compareTo(date) <= 0) {
                    tripNumber = i;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tripNumber;
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


    //Function will return time left [Seconds, Minutes, Hours, Days]
    public float[] TimeLeft(String currentTime, String DestinationTime) {

        float[] timeLeft = new float[4];
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(currentTime);
            d2 = format.parse(DestinationTime);

            //in milliseconds
            long diff = Math.abs(d2.getTime() - d1.getTime());

            timeLeft[0] = diff / 1000 % 60; //Seconds
            timeLeft[1] = diff / (60 * 1000) % 60; //Minutes
            timeLeft[2] = diff / (60 * 60 * 1000) % 24; //Hours
            timeLeft[3] = diff / (24 * 60 * 60 * 1000); //days

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeLeft;
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
}



