package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class ParkingCafeteria {

    Breakfast breakfast;
    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;
    Lunch lunch;

    public ParkingCafeteria() {
        this.breakfast = new Breakfast(false, false, "09:00", "10:00");
        this.midMorningTea = new MidMorningTea(false, false, "10:15", "11:45");
        this.lunch = new Lunch(false, false, "12:45", "13:45");
        this.midEveningTea = new MidEveningTea(false, false, "15:00", "17:30");
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

}
