package com.rohitsuratekar.NCBSinfo.models;

/**
 * Created by Rohit Suratekar on 13-04-16.
 */
public class CommonEventModel {

    private int dataID;
    private String timestamp;
    private String notificationTitle;
    private String date;
    private String time;
    private String venue;
    private String speaker;
    private String CommonItem1;
    private String CommonItem2;
    private String CommonItem3;
    private String datacode;
    private int actioncode;

    public CommonEventModel(int dataID, String timestamp, String notificationTitle, String date, String time, String venue, String speaker, String commonItem1, String commonItem2, String commonItem3, String datacode, int actioncode) {
        this.dataID = dataID;
        this.timestamp = timestamp;
        this.notificationTitle = notificationTitle;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.speaker = speaker;
        this.CommonItem1 = commonItem1;
        this.CommonItem2 = commonItem2;
        this.CommonItem3 = commonItem3;
        this.datacode = datacode;
        this.actioncode = actioncode;
    }

    public CommonEventModel() {

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

    public String getCommonItem1() {
        return CommonItem1;
    }

    public void setCommonItem1(String commonItem1) {
        CommonItem1 = commonItem1;
    }

    public String getCommonItem2() {
        return CommonItem2;
    }

    public void setCommonItem2(String commonItem2) {
        CommonItem2 = commonItem2;
    }

    public String getCommonItem3() {
        return CommonItem3;
    }

    public void setCommonItem3(String commonItem3) {
        CommonItem3 = commonItem3;
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
