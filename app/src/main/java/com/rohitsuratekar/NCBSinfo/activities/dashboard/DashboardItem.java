package com.rohitsuratekar.NCBSinfo.activities.dashboard;

/**
 * Created by Rohit Suratekar on 15-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class DashboardItem {

    private String title;
    private String subtitle;
    private boolean editable;
    private int action;
    private int icon;

    DashboardItem() {
    }

    public DashboardItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
