package com.rohitsuratekar.NCBSinfo.activities.home;

/**
 * Created by Dexter on 10-01-2017.
 */

public class SuggestionModel {

    private String details;
    private int icon;

    public SuggestionModel(String details, int icon) {
        this.details = details;
        this.icon = icon;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
