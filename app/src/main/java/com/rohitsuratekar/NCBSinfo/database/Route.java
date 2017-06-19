package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.rohitsuratekar.NCBSinfo.database.Route.ROUTE_TABLE;

/**
 * Created by Rohit Suratekar on 17-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Entity(tableName = ROUTE_TABLE)
public class Route {

    public static final String ROUTE_TABLE = "routes";

    @PrimaryKey(autoGenerate = true)
    private int routeID;
    @ColumnInfo(name = "origin")
    private String origin;
    @ColumnInfo(name = "destination")
    private String destination;
    @ColumnInfo(name = "type")
    private String type;

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
