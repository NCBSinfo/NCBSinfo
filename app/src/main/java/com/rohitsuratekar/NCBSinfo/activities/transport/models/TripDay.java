package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import com.rohitsuratekar.NCBSinfo.common.ErrorReporting;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

class TripDay {

    private int day;
    private List<String> rawTrips;
    private List<String> todaysTrips;
    private List<String> tomorrowsTrips;

    TripDay(int day, List<String> rawTrips) {
        this.day = day;
        this.rawTrips = rawTrips;
        todaysTrips = new ArrayList<>();
        tomorrowsTrips = new ArrayList<>();
        List<String> sortedTrips = DateConverter.sortStrings(ConverterMode.DATE_FIRST, rawTrips);
        for (int i = 0; i < rawTrips.size(); i++) {
            if (i < sortedTrips.indexOf(rawTrips.get(0))) {
                tomorrowsTrips.add(sortedTrips.get(i));
            } else {
                todaysTrips.add(sortedTrips.get(i));
            }
        }
    }

    int getDay() {
        return day;
    }

    List<String> getRawTrips() {
        return rawTrips;
    }

    List<String> getTodaysTrips() {
        return todaysTrips;
    }

    List<String> getTomorrowsTrips() {
        return tomorrowsTrips;
    }

    int nextTripIndex(Calendar calendar) {
        Date currentDate = new Date(calendar.getTimeInMillis());
        for (String s : todaysTrips) {
            try {
                Calendar c1 = DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s);
                c1.set(Calendar.DATE, calendar.get(Calendar.DATE));
                Date dateItem = new Date(c1.getTimeInMillis());
                if (dateItem.after(currentDate)) {
                    return rawTrips.indexOf(s);
                }
            } catch (ParseException e) {
                ErrorReporting.wrongDateFormat(s);
                e.printStackTrace();
            }
        }
        for (String s : tomorrowsTrips) {
            try {
                Calendar calItem = DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s);
                calItem.set(Calendar.DATE, calendar.get(Calendar.DATE));
                calItem.add(Calendar.DATE, 1);
                Date dateItem = new Date(calItem.getTimeInMillis());
                if (dateItem.after(currentDate)) {
                    return rawTrips.indexOf(s);
                }
            } catch (ParseException e) {
                ErrorReporting.wrongDateFormat(s);
                e.printStackTrace();
            }
        }
        return -1;
    }
}
