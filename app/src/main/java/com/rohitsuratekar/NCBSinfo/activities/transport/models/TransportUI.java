package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class TransportUI {

    private List<String> leftList;
    private List<String> rightList;
    private List<DayTrips> dayTrips;

    public TransportUI(List<DayTrips> dayTrips) {
        this.dayTrips = dayTrips;
        leftList = dayTrips.get(0).getTripsStrings();
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
}
