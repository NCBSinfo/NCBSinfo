package com.rohitsuratekar.NCBSinfo.database.models;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class AlarmModel {
    int id;
    int alarmID;
    String type;
    String trigger;
    String level;
    String extraParameter;
    String extraValue;
    String alarmTime;
    String alarmDate;

    public AlarmModel(int id, int alarmID, String type, String trigger, String level, String extraParameter, String extraValue, String alarmTime, String alarmDate) {
        this.id = id;
        this.alarmID = alarmID;
        this.type = type;
        this.trigger = trigger;
        this.level = level;
        this.extraParameter = extraParameter;
        this.extraValue = extraValue;
        this.alarmTime = alarmTime;
        this.alarmDate = alarmDate;
    }

    public AlarmModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExtraParameter() {
        return extraParameter;
    }

    public void setExtraParameter(String extraParameter) {
        this.extraParameter = extraParameter;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }
}

