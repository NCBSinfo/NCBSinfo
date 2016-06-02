package com.rohitsuratekar.NCBSinfo.models;

public class ExternalModel {

    int id;
    String timestamp;
    String code;
    String title;
    String message;
    String extra;

    public ExternalModel(int id, String timestamp, String code, String title, String message, String extra) {
        this.id = id;
        this.timestamp = timestamp;
        this.code = code;
        this.title = title;
        this.message = message;
        this.extra = extra;
    }

    public ExternalModel() {
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
