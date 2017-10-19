package com.rohitsuratekar.NCBSinfo.activities.contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 14-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ContactModel {

    enum AREA {
        NAME, EXTENSION, LOCATION, DETAILS
    }

    private String type;
    private String name;
    private String location;
    private String primaryExtension;
    private List<String> otherExtensions = new ArrayList<>();
    private String institute;
    private String details;
    private String searchString = "";
    private List<AREA> searchArea = new ArrayList<>();

    public ContactModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrimaryExtension() {
        return primaryExtension;
    }

    public void setPrimaryExtension(String primaryExtension) {
        this.primaryExtension = primaryExtension;
    }

    public List<String> getOtherExtensions() {
        return otherExtensions;
    }

    public void setOtherExtensions(List<String> otherExtensions) {
        this.otherExtensions = otherExtensions;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    void addSearchArea(AREA area) {
        searchArea.add(area);
    }

    public List<AREA> getSearchArea() {
        return searchArea;
    }

    public void clearArea() {
        this.searchArea.clear();
    }
}
