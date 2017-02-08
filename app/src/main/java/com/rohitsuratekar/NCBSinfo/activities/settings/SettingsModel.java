package com.rohitsuratekar.NCBSinfo.activities.settings;

class SettingsModel {

    private String title;
    private String subtitle;
    private int action;
    private int icon;
    private int viewType;

    public SettingsModel(String title, String subtitle, int action) {
        this.title = title;
        this.subtitle = subtitle;
        this.action = action;
    }

    SettingsModel() {
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
