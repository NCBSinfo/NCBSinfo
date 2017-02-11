package com.rohitsuratekar.NCBSinfo.database.models;

public class NotificationModel {

    private int id;
    private String timestamp;
    private String title;
    private String message;
    private String from;
    private String expires;
    private String extraVariables;

    public NotificationModel() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getExtraVariables() {
        return extraVariables;
    }

    public void setExtraVariables(String extraVariables) {
        this.extraVariables = extraVariables;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
