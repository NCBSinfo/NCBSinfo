package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;

public class CurrentMenu {

    CurrentActivity currentActivity;

    public CurrentMenu(CurrentActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public int set() {
        switch (currentActivity) {
            case HOME:
                return R.menu.blank_menu;
            case ONLINE_HOME:
                return R.menu.blank_menu;
            case OFFLINE_HOME:
                return R.menu.blank_menu;
            case LOGIN:
                return R.menu.blank_menu;
            case REGISTRATION:
                return R.menu.blank_menu;
            default:
                return R.menu.base;
        }
    }

}
