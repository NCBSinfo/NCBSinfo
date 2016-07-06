package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class AdminCafeteria {

    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;

    public AdminCafeteria() {

        this.midMorningTea = new MidMorningTea(false, true, "10:15", "12:00");
        this.midEveningTea = new MidEveningTea(false, true, "15:00", "18:00");
    }


    public MidMorningTea getMidMorningTea() {
        return midMorningTea;
    }

    public MidEveningTea getMidEveningTea() {
        return midEveningTea;
    }

}