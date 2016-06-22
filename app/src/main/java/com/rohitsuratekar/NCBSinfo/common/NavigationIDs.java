package com.rohitsuratekar.NCBSinfo.common;

import android.app.Activity;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.lecturehalls.LectureHalls;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.online.dashboard.DashBoard;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMP;

public class NavigationIDs {

    int resourceID;
    Activity activity;

    public NavigationIDs(int resourceID, Activity activity) {
        this.resourceID = resourceID;
        this.activity = activity;
    }

    public Intent getIntent()
    {
        switch (resourceID) {
            case R.id.nav_transport:
                Intent i = new Intent(activity, Transport.class);
                i.putExtra(Transport.INDENT, 0);
                return i;
            case R.id.nav_contacts:
                return new Intent(activity, Contacts.class);
            case R.id.nav_dashboard:
                return new Intent(activity, DashBoard.class);
            case R.id.nav_experimental:
                return new Intent(activity, Experimental.class);
            case R.id.nav_settings:
                return new Intent(activity, Settings.class);
            case R.id.nav_home:
                return new Intent(activity, Home.class);
            case R.id.nav_lecturehall:
                return new Intent(activity, LectureHalls.class);
            case R.id.nav_updates:
                return new Intent(activity, Events.class);
            case R.id.nav_camp:
                return new Intent(activity, CAMP.class);
            default:
                return new Intent(activity, Home.class);
        }
    }
}
