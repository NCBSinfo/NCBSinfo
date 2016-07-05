package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class MainFastFood {


    MidEveningTea midEveningTea;
    Lunch lunch;
    Dinner dinner;

    public MainFastFood() {

        this.lunch = new Lunch(true, true, "10:00", "14:00");
        this.midEveningTea = new MidEveningTea(false, true, "15:00", "18:30");
        this.dinner = new Dinner(false, true, "18:30", "23:30");
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
}
