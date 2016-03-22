package com.rohitsuratekar.NCBSinfo.models;

public class models_userNotifications {

    static int messageID;
    private String timestamp;
    private String notificationTitle;
    private String notificationMessage;
    private String notificationTopic;
    private int showmessage;

    public models_userNotifications(int messageID, String timestamp, String notificationTitle, String notificationMessage, String notificationTopic, int showmessage) {
        models_userNotifications.messageID = messageID;
        this.timestamp = timestamp;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationTopic = notificationTopic;
        this.showmessage = showmessage;
    }

    public models_userNotifications() {

    }

    public static int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        models_userNotifications.messageID = messageID;
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

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationTopic() {
        return notificationTopic;
    }

    public void setNotificationTopic(String notificationTopic) {
        this.notificationTopic = notificationTopic;
    }

    public int getShowmessage() {
        return showmessage;
    }

    public void setShowmessage(int showmessage) {
        this.showmessage = showmessage;
    }
}
