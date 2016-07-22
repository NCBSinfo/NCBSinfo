package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class MainCanteen {

    Breakfast breakfast;
    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;
    Lunch lunch;
    Dinner dinner;

    public MainCanteen() {
        this.breakfast = new Breakfast(true, true, "07:15", "09:20");
        this.midMorningTea = new MidMorningTea(true, true, "10:00", "12:00");
        this.lunch = new Lunch(true, true, "12:30", "13:45");
        this.midEveningTea = new MidEveningTea(false, true, "15:00", "18:00");
        this.dinner = new Dinner(false, true, "19:15", "20:30");
    }

    public Breakfast getBreakfast() {
        return breakfast;
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

    public Dinner getDinner() {
        return dinner;
    }
}
