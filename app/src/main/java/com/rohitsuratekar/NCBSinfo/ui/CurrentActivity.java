package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * This enum will decide activity UI
 * ACTIVITY(DrawerItem)
 * DrawerItem will be item highlighted in navigation drawer
 */
public enum CurrentActivity {
    HOME(R.id.nav_home), //Main Home and Launcher activity
    LOGIN(R.id.nav_dashboard);


    private final int DrawerItem;

    private CurrentActivity(int DrawerItem) {
        this.DrawerItem = DrawerItem;
    }

    public int getDrawerItem() {
        return DrawerItem;
    }
}
