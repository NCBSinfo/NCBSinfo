package com.rohitsuratekar.NCBSinfo.background.remote;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 09-07-16.
 */
public class RemoteModel {

    String status;
    String trigger;
    String level;
    String counter;
    String key;
    String value;
    String extraParameter;

    public RemoteModel() {
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExtraParameter(String extraParameter) {
        this.extraParameter = extraParameter;
    }

    public String getStatus() {
        return status;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getLevel() {
        return level;
    }

    public int getCounter() {
        return Integer.parseInt(counter);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getExtraParameter() {
        return extraParameter;
    }
}
