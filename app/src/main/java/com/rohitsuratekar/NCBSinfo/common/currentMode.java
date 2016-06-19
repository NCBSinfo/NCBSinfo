package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;

public class CurrentMode {

    String switchModeMessage;
    int icon;
    int DrawerHeader;
    int DrawerMenu;
    Context context;

    public CurrentMode(Context context, String section) {
        this.context = context;
        int mode =1;
        if (PreferenceManager.getDefaultSharedPreferences(context).getString(Home.MODE, Home.ONLINE).equals(Home.ONLINE)) {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_online);
            this.icon = R.drawable.icon_wifi_on;
            this.DrawerHeader = R.layout.nav_header_online;
            mode = 1;
        } else {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_offline);
            this.icon = R.drawable.icon_wifi_off;
            this.DrawerHeader = R.layout.nav_header_offline;
            mode = 2;
        }

        switch (section){
            case Transport.MODE_CONSTANT: activateTransport(mode); break;
            case Contacts.MODE_CONSTANT: activateContacts(mode); break;
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


    private void activateTransport(int mode){
        if (mode==1) {
            this.DrawerMenu = R.menu.transport_drawer_online;
        } else {
            this.DrawerMenu = R.menu.transport_drawer_offline;
        }
    }

    private void activateContacts(int mode){
        if (mode==1) {
            this.DrawerMenu = R.menu.contact_drawer_online;
        } else {
            this.DrawerMenu = R.menu.contact_drawer_offline;
        }
    }
}
