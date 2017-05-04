package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class Trip {

    private String time;
    private boolean isToday;
    private boolean isInSameList = true;
    private int index;

    public Trip(String time) {
        this.time = time;
    }

    public Trip() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public Calendar getRealTime() {
        try {
            return DateConverter.convertToCalender(ConverterMode.DATE_FIRST, time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.error("Unable to parse " + time);
        }
        return null;
    }

    public boolean isAfter(Calendar calendar) {
        Date date1 = new Date(getRealTime().getTimeInMillis());
        if (isToday) {
            Date date2 = new Date(calendar.getTimeInMillis());
            return date1.after(date2);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(getRealTime().getTimeInMillis());
            cal.add(Calendar.DATE, 1);
            Date currentTrip = new Date(cal.getTimeInMillis());
            Date givenTime = new Date(calendar.getTimeInMillis());
            return currentTrip.after(givenTime);
        }
    }

    public String getFormatted() {
        try {
            return DateConverter.changeFormat(ConverterMode.DATE_FIRST, time, "hh:mm a");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.error("Unable to parse " + time);
        }
        return "--:--";
    }

    public void checkDay(Calendar firstTrip) {
        Date date1 = new Date(getRealTime().getTimeInMillis());
        Date date2 = new Date(firstTrip.getTimeInMillis());
        isToday = date1.after(date2) || date1.getTime() == date2.getTime();
    }

    public boolean isInSameList() {
        return isInSameList;
    }

    public void setInSameList(boolean inSameList) {
        isInSameList = inSameList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
