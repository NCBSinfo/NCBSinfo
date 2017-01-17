package com.rohitsuratekar.NCBSinfo.database.route;

import com.secretbiology.helpers.general.ConverterMode;

import java.util.ArrayList;
import java.util.List;

import static com.secretbiology.helpers.general.DateConverter.sortStrings;

public class Day {
    private int day;
    private List<String> tripList;
    private List<String> originalList;

    public Day(int day, List<String> tripList) {
        this.day = day;
        this.tripList = new ArrayList<>(tripList);
        this.originalList = new ArrayList<>(tripList);
    }

    int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getTripList() {
        return this.originalList;
    }

    public void setTripList(List<String> tripList) {
        this.originalList = new ArrayList<>(tripList);
        this.tripList = originalList;
    }

    List<String> getTodayTrips() {
        List<String> list = new ArrayList<>();
        List<String> sorted = sortStrings(ConverterMode.DATE_FIRST, tripList);
        for (int i = 0; i < tripList.size(); i++) {
            if (i >= sorted.indexOf(originalList.get(0))) {
                list.add(sorted.get(i));
            }
        }
        return list;
    }

    List<String> getNextDayTrips() {
        List<String> list = new ArrayList<>();
        List<String> sorted = sortStrings(ConverterMode.DATE_FIRST, tripList);
        for (int i = 0; i < tripList.size(); i++) {
            if (i < sorted.indexOf(originalList.get(0))) {
                list.add(sorted.get(i));
            }
        }
        return list;
    }
}
