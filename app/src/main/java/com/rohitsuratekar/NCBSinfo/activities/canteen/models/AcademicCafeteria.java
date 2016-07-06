package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

import java.util.Calendar;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class AcademicCafeteria {

    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;
    Lunch lunch;

    public AcademicCafeteria(int DayofWeek) {

        this.midMorningTea = new MidMorningTea(false, true, "09:30", "12:45");
        this.lunch = new Lunch(false, true, "12:45", "13:45");

        if (DayofWeek == Calendar.SATURDAY) {
            this.midEveningTea = new MidEveningTea(false, true, "15:00", "17:30");
        } else {
            this.midEveningTea = new MidEveningTea(false, true, "15:00", "18:00");
        }
    }


    public MidMorningTea getMidMorningTea() {
        return midMorningTea;
    }

    public MidEveningTea getMidEveningTea() {
        return midEveningTea;
    }

    public Lunch getLunch() {
        return lunch;
    }

}
