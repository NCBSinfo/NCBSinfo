package com.rohitsuratekar.NCBSinfo.background.alarms;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class AlarmsHelper implements AlarmConstants {

    private final String TAG = getClass().getSimpleName();

    public alarmTriggers getTrigger(String name) {
        for (alarmTriggers t : alarmTriggers.values()) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public String getTodaysDate() {
        return new DateConverters().convertToString(new Date(), DateFormats.DATE_STANDARD);
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
        return new DateConverters().convertToCalendar(
                alarmModel.getAlarmDate() + " " + alarmModel.getAlarmTime()).getTimeInMillis();
    }


    public int createTransportID(Routes routes, int DayOfWeek, String time) {
        String changed = time.trim().replace(":", "");
        return routes.getRouteNo() * 100000 + DayOfWeek * 1000 + Integer.parseInt(changed);
    }

}
