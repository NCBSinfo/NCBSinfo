package com.rohitsuratekar.NCBSinfo.database.models;

public class TalkModel {

    private int dataID;
    private String timestamp;
    private String notificationTitle;
    private String date;
    private String time;
    private String venue;
    private String speaker;
    private String affilication;
    private String title;
    private String host;
    private String datacode;
    private int actioncode;

    public TalkModel(int dataID, String timestamp, String notificationTitle, String date, String time, String venue, String speaker, String affilication, String title, String host, String datacode, int actioncode) {
        this.dataID = dataID;
        this.timestamp = timestamp;
        this.notificationTitle = notificationTitle;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.speaker = speaker;
        this.affilication = affilication;
        this.title = title;
        this.host = host;
        this.datacode = datacode;
        this.actioncode = actioncode;
    }

    public TalkModel() {


    }

    public int getDataID() {
        return dataID;
    }

    public void setDataID(int dataID) {
        this.dataID = dataID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getAffilication() {
        return affilication;
    }

    public void setAffilication(String affilication) {
        this.affilication = affilication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatacode() {
        return datacode;
    }

    public void setDatacode(String datacode) {
        this.datacode = datacode;
    }

    public int getActioncode() {
        return actioncode;
    }

    public void setActioncode(int actioncode) {
        this.actioncode = actioncode;
    }
}