package com.rohitsuratekar.NCBSinfo.models;

public class LogModel {

    private int id;
    private String timestamp;
    private String message;
    private String details;
    private String category;
    private int statuscode;
    private int status;

    public LogModel() {
    }

    public LogModel(int id, String timestamp, String message, String details, String category, int statuscode, int status) {
        this.id = id;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.category = category;
        this.statuscode = statuscode;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
