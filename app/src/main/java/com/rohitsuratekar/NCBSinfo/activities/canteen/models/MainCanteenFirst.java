package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class MainCanteenFirst {


    Lunch lunch;


    public MainCanteenFirst() {
        this.lunch = new Lunch(false, false, "12:45", "13:45");
    }


    public Lunch getLunch() {
        return lunch;
    }


}
