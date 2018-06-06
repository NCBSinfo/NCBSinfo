package com.rohitsuratekar.NCBSinfo.fragments.settings;

/**
 * Created by SecretAdmin on 10/30/2017 for NCBSinfo.
 * All code is released under MIT License.
 */

class SettingsModel {

    private int icon;
    private String title;
    private int viewType;
    private String description;
    private int action;
    private boolean isDisabled;

    SettingsModel(int viewType) {
        this.viewType = viewType;
    }

    SettingsModel(String title) {
        this.viewType = SettingsActions.VIEW_HEADER;
        this.title = title;
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

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
