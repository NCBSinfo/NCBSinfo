package com.rohitsuratekar.NCBSinfo.fragments.contacts;

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

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    String getPrimaryExtension() {
        return primaryExtension;
    }

    void setPrimaryExtension(String primaryExtension) {
        this.primaryExtension = primaryExtension;
    }

    List<String> getOtherExtensions() {
        return otherExtensions;
    }

    void setOtherExtensions(List<String> otherExtensions) {
        this.otherExtensions = otherExtensions;
    }

    String getInstitute() {
        return institute;
    }

    void setInstitute(String institute) {
        this.institute = institute;
    }

    String getDetails() {
        return details;
    }

    void setDetails(String details) {
        this.details = details;
    }

    String getSearchString() {
        return searchString;
    }

    void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    void addSearchArea(AREA area) {
        searchArea.add(area);
    }

    List<AREA> getSearchArea() {
        return searchArea;
    }

    void clearArea() {
        this.searchArea.clear();
    }
}
