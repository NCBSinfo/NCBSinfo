package com.rohitsuratekar.NCBSinfo.common.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities {

    private final String TAG = getClass().getSimpleName();

    public String timeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public int reverseTimestamp(String timestamp) {
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in reverseTimestamp");
        }
        return (int) dt.getTime();
    }


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

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String makeReadableTime(int timeInmilliseconds) {
        Date cal = new Date();
        long diff = ((int) cal.getTime() - timeInmilliseconds);
        long seconds = diff / (1000); //Seconds
        long min = diff / (1000 * 60); //Minutes
        long hour = diff / (1000 * 60 * 60); //Hours
        cal.setTime(timeInmilliseconds);
        String returnString;
        if (min <= 1) {
            returnString = seconds + " seconds ago";
        } else if (min <= 10) {
            returnString = "Few minutes ago";
        } else if (hour == 0 && min > 10) {
            returnString = min + " minutes ago";
        } else if (hour >= 1 && hour < 5) {
            returnString = "Few hours ago";
        } else if (hour >= 5 && hour < 24) {
            returnString = hour + " hours ago";
        } else if (hour >= 24 && hour < 48) {
            returnString = "Yesterday";
        } else if (hour >= 48 && hour < 120) {
            returnString = "Few days ago";
        } else {
            returnString = new SimpleDateFormat("d MMM", Locale.getDefault()).format(timeInmilliseconds);
        }
        return returnString;
    }

    public List<TalkModel> getUpcomigTalks(Context context, Date targetDate) {
        List<TalkModel> allList = new TalkData(context).getAll();
        List<TalkModel> returnList = new ArrayList<>();
        for (TalkModel talk : allList) {
            Date tempdate = new Utilities().convertToTalkDate(talk.getDate(), talk.getTime());
            //Upcoming events will be before target date and after current date
            if (tempdate.before(targetDate) && tempdate.after(new Date())) {
                returnList.add(talk);
            }
        }
        return returnList;
    }

    public int getMilliseconds(String timestamp) {
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in getMilliseconds");
        }
        return (int) dt.getTime();
    }

}
