package com.rohitsuratekar.NCBSinfo.activities.transport.routes;

import com.rohitsuratekar.NCBSinfo.activities.Constants;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;
import com.secretbiology.helpers.general.DateConverter;

import java.util.Calendar;

public class RouteModel {

    private String origin;
    private String destination;
    private int dayOfWeek;
    private String trips;
    private String firstTrip;
    private TransportType type;
    private int icon;
    private String author;
    private String timestampCreation;
    private String timestampModified;
    private String trigger;

    public RouteModel() {
        setTime();
    }

    public RouteModel(String origin, String destination, int dayOfWeek, String trips, String firstTrip, TransportType type) {
        this.origin = origin;
        this.destination = destination;
        this.dayOfWeek = dayOfWeek;
        this.trips = trips;
        this.firstTrip = firstTrip;
        this.type = type;
        setTime();
    }

    public RouteModel(String origin, String destination, int dayOfWeek, String trips, String firstTrip) {
        this.origin = origin;
        this.destination = destination;
        this.dayOfWeek = dayOfWeek;
        this.trips = trips;
        this.firstTrip = firstTrip;
        setTime();
    }

    public RouteModel(String origin, String destination, int dayOfWeek, String trips, String firstTrip, TransportType type, int icon) {
        this.origin = origin;
        this.destination = destination;
        this.dayOfWeek = dayOfWeek;
        this.trips = trips;
        this.firstTrip = firstTrip;
        this.type = type;
        this.icon = icon;
        setTime();
    }

    public RouteModel(String origin, String destination, String trips, String firstTrip) {
        this.origin = origin;
        this.destination = destination;
        this.trips = trips;
        this.firstTrip = firstTrip;
        setTime();
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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTrips() {
        return trips;
    }

    public void setTrips(String trips) {
        this.trips = trips;
    }

    public String getFirstTrip() {
        return firstTrip;
    }

    public void setFirstTrip(String firstTrip) {
        this.firstTrip = firstTrip;
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestampCreation() {
        return timestampCreation;
    }


    public String getTimestampModified() {
        return timestampModified;
    }

    public void setTimestampModified(String timestampModified) {
        this.timestampModified = timestampModified;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    private void setTime() {
        //TODO: add user info
        this.author = "unknown";
        this.trigger = "null";
        this.timestampCreation = DateConverter.convertToString(Calendar.getInstance(), Constants.TIMESTAMP_FORMAT);
        this.timestampModified = this.timestampCreation;
    }
}
