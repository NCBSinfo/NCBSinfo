package com.rohitsuratekar.NCBSinfo.models;

public class DataModel {
    private int dataID;
    private String timestamp;
    private String notificationTitle;
    private String date;
    private String time;
    private String venue;
    private String speaker;
    private String talkabstract;
    private String url;
    private String nextspeaker;
    private String datacode;
    private int actioncode;

    public DataModel(int dataID, String timestamp, String notificationTitle, String date, String time, String venue, String speaker, String talkabstract, String url, String nextspeaker, String datacode, int actioncode) {
        this.dataID = dataID;
        this.timestamp = timestamp;
        this.notificationTitle = notificationTitle;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.speaker = speaker;
        this.talkabstract = talkabstract;
        this.url = url;
        this.nextspeaker = nextspeaker;
        this.datacode = datacode;
        this.actioncode = actioncode;
    }

    public DataModel() {
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

    public String getTalkabstract() {
        return talkabstract;
    }

    public void setTalkabstract(String talkabstract) {
        this.talkabstract = talkabstract;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNextspeaker() {
        return nextspeaker;
    }

    public void setNextspeaker(String nextspeaker) {
        this.nextspeaker = nextspeaker;
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
