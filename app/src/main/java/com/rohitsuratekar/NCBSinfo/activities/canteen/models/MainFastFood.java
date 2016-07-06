package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

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

    public MainFastFood() {

        this.midMorningTea = new MidMorningTea(false, true, "10:00", "12:30");
        this.lunch = new Lunch(true, true, "12:30", "14:00");
        this.midEveningTea = new MidEveningTea(true, true, "15:00", "18:30");
        this.dinner = new Dinner(true, true, "18:30", "23:30");

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
