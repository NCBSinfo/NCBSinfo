package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.rohitsuratekar.NCBSinfo.database.RouteData.ROUTE_TABLE;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

@Entity(tableName = ROUTE_TABLE)
public class RouteData {

    static final String ROUTE_TABLE = "routes";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "routeID")
    private int routeID;
    @ColumnInfo(name = "origin")
    private String origin;
    @ColumnInfo(name = "destination")
    private String destination;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "favorite")
    private boolean favorite;
    @ColumnInfo(name = "creation")
    private String createdOn;
    @ColumnInfo(name = "modified")
    private String modifiedOn;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "synced")
    private String synced;


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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }
}