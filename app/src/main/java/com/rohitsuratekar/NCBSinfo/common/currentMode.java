package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.lecturehalls.LectureHalls;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.online.dashboard.DashBoard;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMP;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMPevents;

public class CurrentMode implements UserInformation {

    String switchModeMessage;
    int icon;
    int DrawerHeader;
    int DrawerMenu;
    Context context;

    public CurrentMode(Context context, String section) {
        this.context = context;
        int mode = 1;
        String currentMode = PreferenceManager.getDefaultSharedPreferences(context).getString(Home.MODE, Home.ONLINE);
        if (currentMode.equals(Home.ONLINE)) {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_online);
            this.icon = R.drawable.icon_wifi_on;
            this.DrawerHeader = R.layout.nav_header_online;
            mode = 1;
        } else if (currentMode.equals(registration.camp16.CAMP_MODE)) {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_camp);
            this.icon = R.drawable.icon_camp;
            this.DrawerHeader = R.layout.nav_header_online;
            mode = 3;
        } else {
            this.switchModeMessage = context.getResources().getString(R.string.warning_mode_change_offline);
            this.icon = R.drawable.icon_wifi_off;
            this.DrawerHeader = R.layout.nav_header_offline;
            mode = 2;
        }

        switch (section) {
            case Transport.MODE_CONSTANT:
                activateTransport(mode);
                break;
            case Contacts.MODE_CONSTANT:
                activateContacts(mode);
                break;
            case LectureHalls.MODE_CONSTANT:
                activateLectureHall(mode);
                break;
            case Experimental.MODE_CONSTANT:
                activateExperimental(mode);
                break;
            case CAMP.MODE_CONSTANT:
                activateCAMP(mode);
                break;
            case Events.MODE_CONSTANT:
                activateEvents(mode);
                break;
            case Information.MODE_CONSTANT:
                activateInformation(mode);
                break;
            case DashBoard.MODE_CONSTANT:
                activateDashboard(mode);
                break;
            case CAMPevents.MODE_CONSTANT:
                activateCampEvents(mode);
                break;
            default:
                activateContacts(mode);
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


    private void activateTransport(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.transport_drawer_online;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.transport_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.transport_drawer_offline;
        }
    }

    private void activateContacts(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.contact_drawer_online;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.contact_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.contact_drawer_offline;
        }
    }

    private void activateLectureHall(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.lecture_halls_drawer_online;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.lecture_halls_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.lecture_halls_drawer_offline;
        }
    }

    private void activateDashboard(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.dash_board_drawer;
        } else {
            this.DrawerMenu = R.menu.dash_board_drawer;
        }
    }

    private void activateExperimental(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.experimental_drawer;
        } else {
            this.DrawerMenu = R.menu.experimental_drawer;
        }
    }

    private void activateCAMP(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.camp_drawer;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.camp_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.camp_drawer;
        }
    }

    private void activateEvents(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.events_drawer;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.events_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.events_drawer;
        }
    }

    private void activateInformation(int mode) {
        if (mode == 1) {
            this.DrawerMenu = R.menu.information_drawer_online;
        } else if (mode == 3) {
            this.DrawerMenu = R.menu.information_drawer_camp;
        } else {
            this.DrawerMenu = R.menu.information_drawer_offline;
        }
    }

    private void activateCampEvents(int mode) {
       this.DrawerMenu = R.menu.camp_events_drawer;
    }
}
