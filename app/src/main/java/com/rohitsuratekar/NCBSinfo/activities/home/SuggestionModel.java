package com.rohitsuratekar.NCBSinfo.activities.home;

class SuggestionModel {
    private String details;
    private int icon;
    private int action;

    SuggestionModel(String details, int icon) {
        this.details = details;
        this.icon = icon;
    }

    SuggestionModel() {
    }

    public int getAction() {
        return action;
    }

    public SuggestionModel setAction(int action) {
        this.action = action;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public SuggestionModel setDetails(String details) {
        this.details = details;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public SuggestionModel setIcon(int icon) {
        this.icon = icon;
        return this;
    }
}
