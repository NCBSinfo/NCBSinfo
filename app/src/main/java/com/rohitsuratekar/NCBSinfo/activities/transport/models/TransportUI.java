package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportMethods;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class TransportUI {

    private List<String> leftList;
    private List<String> rightList;
    private List<DayTrips> dayTrips;
    private boolean isRegular;
    private Calendar calendar;
    private Trip nextTrip;

    TransportUI(List<DayTrips> dayTrips) {
        this.calendar = Calendar.getInstance();
        this.dayTrips = dayTrips;
        nextTrip = TransportMethods.nextTrip(TransportMethods.getWeekMap(dayTrips), calendar);
        SparseArray<DayTrips> map = new SparseArray<>();
        for (DayTrips d : dayTrips) {
            map.put(d.getDay(), d);
        }
        this.isRegular = map.size() == 2 && map.get(Calendar.SUNDAY) != null;
        if (isRegular) {
            for (int i = 0; i < map.size(); i++) {
                if (map.valueAt(i).getDay() == Calendar.SUNDAY) {
                    rightList = map.valueAt(i).getTripsStrings();
                } else {
                    leftList = map.valueAt(i).getTripsStrings();
                }
            }
        } else {
            leftList = dayTrips.get(0).getTripsStrings();
            rightList = dayTrips.get(0).getTripsStrings();
            //TODO  get correct trip based on current calender. Use 'map' instead daytrips
        }

    }

    public List<String> getLeftList() {
        return leftList;
    }

    public void setLeftList(List<String> leftList) {
        this.leftList = leftList;
    }

    public List<String> getRightList() {
        return rightList;
    }

    public void setRightList(List<String> rightList) {
        this.rightList = rightList;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public void setRegular(boolean regular) {
        isRegular = regular;
    }

    public boolean isSingleValued() {
        return dayTrips.size() == 1;
    }

    public Trip getNextTrip() {
        return nextTrip;
    }
}
