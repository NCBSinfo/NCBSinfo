package com.rohitsuratekar.NCBSinfo.models;

public class ConferenceModel {
    int id;
    String timestamp;
    String code;
    String eventTitle;
    String eventHost;
    String eventStartTime;
    String eventEndTime;
    String eventDate;
    String eventVenue;
    String eventMessage;
    int updateCounter;

    public ConferenceModel(int id, String timestamp, String code, String eventTitle, String eventHost, String eventStartTime, String eventEndTime, String eventDate, String eventVenue, String eventMessage, int updateCounter) {
        this.id = id;
        this.timestamp = timestamp;
        this.code = code;
        this.eventTitle = eventTitle;
        this.eventHost = eventHost;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventMessage = eventMessage;
        this.updateCounter = updateCounter;
    }

    public ConferenceModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public int getUpdateCounter() {
        return updateCounter;
    }

    public void setUpdateCounter(int updateCounter) {
        this.updateCounter = updateCounter;
    }
}
