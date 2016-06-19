package com.rohitsuratekar.NCBSinfo.database.models;


public class NotificationModel {

    int id;
    String timestamp;
    String title;
    String message;
    String topic;
    String from;
    String extraVariables;

    public NotificationModel(int id, String timestamp, String title, String message, String topic, String from, String extraVariables) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.message = message;
        this.topic = topic;
        this.from = from;
        this.extraVariables = extraVariables;
    }

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
}
