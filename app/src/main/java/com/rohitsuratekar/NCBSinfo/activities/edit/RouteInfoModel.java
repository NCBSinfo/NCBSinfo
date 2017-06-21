package com.rohitsuratekar.NCBSinfo.activities.edit;

/**
 * Created by Rohit Suratekar on 21-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class RouteInfoModel {
    private String origin;
    private String destination;
    private int type;
    private int frequency;

    RouteInfoModel() {
        origin = "";
        destination = "";
        type = 0;
        frequency = 1;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
