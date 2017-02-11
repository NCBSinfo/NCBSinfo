package com.rohitsuratekar.NCBSinfo.activities.transport;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransportMethods {

    public List<String> nextTransport(Calendar cal, Route route) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        List<String> nextTrips = new ArrayList<>();
        List<String> allTrips = new ArrayList<>(getRefinedList(calendar, route));

        String currentString = DateConverter.convertToString(calendar, "HH:mm");
        allTrips.add(currentString);
        allTrips = DateConverter.sortStrings(ConverterMode.DATE_FIRST, allTrips);

        for (int i = 0; i < allTrips.size(); i++) {
            if (i > allTrips.indexOf(currentString)) {
                nextTrips.add(allTrips.get(i));
            }
        }

        while (nextTrips.size() < 5) {
            calendar.add(Calendar.DATE, 1);
            nextTrips.addAll(getRefinedList(calendar, route));
        }
        return nextTrips;
    }

    public boolean isNextTransportToday(Calendar cal, Route route) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        List<String> allTrips = new ArrayList<>(getRefinedList(calendar, route));
        String currentString = DateConverter.convertToString(calendar, "HH:mm");
        allTrips.add(currentString);
        allTrips = DateConverter.sortStrings(ConverterMode.DATE_FIRST, allTrips);
        for (int i = 0; i < allTrips.size(); i++) {
            if (i > allTrips.indexOf(currentString)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getRefinedList(Calendar calendar, Route route) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        List<String> combinedTrips = new ArrayList<>();
        List<String> today = route.getMap().get(cal.get(Calendar.DAY_OF_WEEK), route.getDefaultList());
        String firstTrip = today.get(0);
        today = DateConverter.sortStrings(ConverterMode.DATE_FIRST, today);
        for (int i = 0; i < today.size(); i++) {
            if (i >= today.indexOf(firstTrip)) {
                combinedTrips.add(today.get(i));
            }
        }



        cal.add(Calendar.DATE, -1);
        List<String> yesterday = route.getMap().get(cal.get(Calendar.DAY_OF_WEEK), route.getDefaultList());
        firstTrip = yesterday.get(0);
        yesterday = DateConverter.sortStrings(ConverterMode.DATE_FIRST, yesterday);
        for (int i = 0; i < yesterday.size(); i++) {
            if (i < yesterday.indexOf(firstTrip)) {
                combinedTrips.add(yesterday.get(i));
            }
        }

        return new ArrayList<>(DateConverter.sortStrings(ConverterMode.DATE_FIRST, combinedTrips));
    }
}
