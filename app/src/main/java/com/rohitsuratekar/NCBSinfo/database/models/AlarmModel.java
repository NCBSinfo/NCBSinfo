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
    String alarmTime;
    String alarmDate;
    String extraParameter;
    String extraValue;

    public AlarmModel(int id, int alarmID, String type, String trigger, String level, String alarmTime, String alarmDate, String extraParameter, String extraValue) {
        this.id = id;
        this.alarmID = alarmID;
        this.type = type;
        this.trigger = trigger;
        this.level = level;
        this.alarmTime = alarmTime;
        this.alarmDate = alarmDate;
        this.extraParameter = extraParameter;
        this.extraValue = extraValue;
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

    public String getExtraParameter() {
        return extraParameter;
    }

    public void setExtraParameter(String extraParamete) {
        this.extraParameter = extraParamete;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}

