package com.rohitsuratekar.NCBSinfo.activities.experimental.holidays;

import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 06-07-16.
 */
public class HolidayModel {
    String title;
    Date date;

    public HolidayModel(String title, String date) {
        this.title = title;
        this.date = new Converters().convertToDate(date);
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

}
