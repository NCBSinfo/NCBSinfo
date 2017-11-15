package com.rohitsuratekar.NCBSinfo.activities.locations;

/**
 * Created by Rohit Suratekar on 15-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class LocationModel {

    private String name;
    private String oldName;
    private String details;
    private String building;
    private int floor;
    private String type;

    LocationModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
