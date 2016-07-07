package com.rohitsuratekar.NCBSinfo.utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Converters {

    private final String TAG = getClass().getSimpleName();

    public Date convertToTalkDate(String Date, String Time) {
        Date returnDate = new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String DateString = Date + " " + Time;
        try {
            returnDate = eventFormat.parse(DateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in convertToDate :" + DateString);
        }
        return returnDate;
    }

    public String[] stringToarray(String input) {
        List<String> output = new ArrayList<>();
        input = input.replace("{", "");
        input = input.replace("}", "");
        String[] split = input.split(",");
        for (String s : split) {
            s = s.replace("\"", "");
            output.add(s.replaceAll("\\s+", ""));
        }
        return output.toArray(new String[output.size()]);
    }

    //Converts "HH:mm" format to "hh:mm a" format
    public String[] convertToSimpleDate(String[] trips) {
        String[] finalStrings = new String[trips.length];
        for (int i = 0; i < trips.length; i++) {
            finalStrings[i] = convertToSimpleDate(trips[i]);
        }
        return finalStrings;
    }

    //Converts "HH:mm" format to "hh:mm a" format
    public String convertToSimpleDate(String trip) {
        String finalStrings = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(trip);
            finalStrings = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in convertToDate :" + trip);
        }
        return finalStrings;
    }

    /**
     * @param time : Simple "HH:mm" time
     * @return : Calender with today's date and that time
     */

    public Calendar convertToCalender(String time) {
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date outPutDate = new Date();
        try {
            String day = dateFormat.format(new Date());
            outPutDate = fullFormat.parse(day + " " + time.trim().replace(".", ":"));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in convertToCalender :" + time);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(outPutDate);
        return cal;
    }

    /**
     *
     * @param date : String in "dd/MM/yyyy" format
     * @return : Date with today's time attached
     */
    public Date convertToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date outPutDate = new Date();
        try {
            outPutDate = dateFormat.parse(date.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in convertToDate :" + date);
        }
        return outPutDate;
    }

    public String calenderToDate(Calendar calendar){
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
    }

    public String calenderToTime(Calendar calendar){
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
    }

}
