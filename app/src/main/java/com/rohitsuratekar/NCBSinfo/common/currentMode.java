package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;

public class CurrentMode {

    String switchModeMessage;
    int icon;
    int DrawerHeader;
    int DrawerMenu;

    public CurrentMode(Context context, String status) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getString(Home.MODE, Home.ONLINE).equals(status)) {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_online);
            this.icon = R.drawable.icon_wifi_on;
            this.DrawerHeader = R.layout.nav_header_online;
            this.DrawerMenu = R.menu.transport_drawer_online;
        } else {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_offline);
            this.icon = R.drawable.icon_wifi_off;
            this.DrawerHeader = R.layout.nav_header_offline;
            this.DrawerMenu = R.menu.transport_drawer_offline;
        }
    }

    public int getIcon() {
        return icon;
    }

    public String getSwitchModeMessage() {
        return switchModeMessage;
    }

    public int getDrawerHeader() {
        return DrawerHeader;
    }

    public int getDrawerMenu() {
        return DrawerMenu;
    }
}
