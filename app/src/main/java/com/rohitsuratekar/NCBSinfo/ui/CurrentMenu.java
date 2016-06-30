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
                return R.menu.base;
            default:
                return R.menu.base;
        }
    }

}
