package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class Breakfast {
    boolean isSundayOpen;
    boolean isSaturdayOpen;
    String startTime;
    String endTime;

    protected Breakfast(boolean isSundayOpen, boolean isSaturdayOpen, String startTime, String endTime) {
        this.isSundayOpen = isSundayOpen;
        this.isSaturdayOpen = isSaturdayOpen;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isSundayOpen() {
        return isSundayOpen;
    }

    public void setSundayOpen(boolean sundayOpen) {
        isSundayOpen = sundayOpen;
    }

    public boolean isSaturdayOpen() {
        return isSaturdayOpen;
    }

    public void setSaturdayOpen(boolean saturdayOpen) {
        isSaturdayOpen = saturdayOpen;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
