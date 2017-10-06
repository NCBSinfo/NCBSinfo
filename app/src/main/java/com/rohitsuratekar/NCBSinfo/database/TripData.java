package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import static com.rohitsuratekar.NCBSinfo.database.TripData.TRIPS_TABLE;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Entity(tableName = TRIPS_TABLE)
@TypeConverters(ListConverter.class)
public class TripData {
    static final String TRIPS_TABLE = "trips";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tripID")
    private int tripID;
    @ColumnInfo(name = "routeID")
    private int routeID;
    @ColumnInfo(name = "trips")
    private List<String> trips;
    @ColumnInfo(name = "day")
    private int day;

    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
