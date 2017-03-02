package com.rohitsuratekar.NCBSinfo.activities.dashboard;

class DashboardModel {
    private String title;
    private String subtitle;
    private int icon;
    private int actionCode;

    DashboardModel() {
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getSubtitle() {
        return subtitle;
    }

    void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    int getIcon() {
        return icon;
    }

    void setIcon(int icon) {
        this.icon = icon;
    }

    int getActionCode() {
        return actionCode;
    }

    void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }
}
