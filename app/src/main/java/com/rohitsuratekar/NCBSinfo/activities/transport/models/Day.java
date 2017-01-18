package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to arrange daily trips
 */
public class Day {

    private List<String> tomorrowTrips = new ArrayList<>();
    private List<String> allTrips;
    private List<String> todaysTrips = new ArrayList<>();
    private int day;

    public Day(int day, List<String> allTrips) {
        this.day = day;
        this.allTrips = new ArrayList<>(allTrips);
        List<String> chronological = DateConverter.sortStrings(ConverterMode.DATE_FIRST, allTrips);
        for (int i = 0; i < chronological.size(); i++) {
            if (i < chronological.indexOf(this.allTrips.get(0))) {
                tomorrowTrips.add(chronological.get(i));
            } else {

                todaysTrips.add(chronological.get(i));
            }
        }
    }

    public List<String> getTomorrowTrips() {
        return tomorrowTrips;
    }

    public List<String> getAllTrips() {
        return allTrips;
    }

    public List<String> getTodaysTrips() {
        return todaysTrips;
    }

    public int getDay() {
        return day;
    }
}
