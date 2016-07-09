package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

import java.util.Calendar;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class MainFastFood {


    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;
    Lunch lunch;
    Dinner dinner;

    public MainFastFood(int DayofWeek) {

        this.midMorningTea = new MidMorningTea(false, true, "10:00", "12:30");
        this.lunch = new Lunch(false, true, "12:30", "14:00");
        this.midEveningTea = new MidEveningTea(true, true, "15:00", "19:15");
        if(DayofWeek== Calendar.SUNDAY){
            this.dinner = new Dinner(true, true, "19:15", "23:30");
        }
        else {
            this.dinner = new Dinner(true, true, "19:15", "23:30");
        }

    }

    public MidEveningTea getMidEveningTea() {
        return midEveningTea;
    }

    public Lunch getLunch() {
        return lunch;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public MidMorningTea getMidMorningTea() {
        return midMorningTea;
    }
}
