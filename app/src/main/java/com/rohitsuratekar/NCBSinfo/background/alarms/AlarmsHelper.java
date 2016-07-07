package com.rohitsuratekar.NCBSinfo.background.alarms;

import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class AlarmsHelper implements AlarmConstants {

    private final String TAG = getClass().getSimpleName();

    public triggers getTrigger(String name) {
        for (triggers t : triggers.values()) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public String getTodaysDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public String getTimeByHoursOfDay(int hourOfDay) {
        String value = String.valueOf(hourOfDay);
        //If hour is set in single digit
        if (value.length() == 1) {
            value = "0" + value;
        }
        return value + ":00";
    }

    public long getAlarmMiliseconds(AlarmModel alarmModel) {
        SimpleDateFormat alarmFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String temp = alarmModel.getAlarmDate() + " " + alarmModel.getAlarmTime();
        Date date = new Date();
        try {
            date = alarmFormat.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing date in getAlarmString : " + temp);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

}
