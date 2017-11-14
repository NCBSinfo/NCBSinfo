package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rohit Suratekar on 06-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class RouteModel {
    @SerializedName("key")
    @Expose
    private int key;
    @SerializedName("route")
    @Expose
    private int route;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("trips")
    @Expose
    private List<String> trips;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("syncedOn")
    @Expose
    private String syncedOn;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("databaseID")
    @Expose
    private String databaseID;
    @SerializedName("trigger")
    @Expose
    private String trigger;
    @SerializedName("notes")
    @Expose
    private String notes;

    public RouteModel() {
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSyncedOn() {
        return syncedOn;
    }

    public void setSyncedOn(String syncedOn) {
        this.syncedOn = syncedOn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}