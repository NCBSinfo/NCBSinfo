package com.rohitsuratekar.NCBSinfo.activities.contacts;

/**
 * Created by Rohit Suratekar on 20-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ContactDetailsModel {
    private String name;
    private String details;
    private boolean isHeader;
    private boolean hasSubHeader;

    ContactDetailsModel() {
    }

    ContactDetailsModel(String name) {
        this.isHeader = true;
        this.hasSubHeader = false;
        this.name = name;
    }

    ContactDetailsModel(String name, boolean isHeader) {
        this.isHeader = isHeader;
        this.hasSubHeader = false;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getDetails() {
        return details;
    }

    void setDetails(String details) {
        this.details = details;
    }

    boolean isHeader() {
        return isHeader;
    }

    void setHeader(boolean header) {
        isHeader = header;
    }

    boolean hasSubHeader() {
        return hasSubHeader;
    }

    void setHasSubHeader(boolean hasSubHeader) {
        this.hasSubHeader = hasSubHeader;
    }
}
