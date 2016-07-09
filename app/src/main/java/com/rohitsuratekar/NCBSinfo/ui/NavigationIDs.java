package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.activities.canteen.Canteen;
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoard;
import com.rohitsuratekar.NCBSinfo.activities.events.Events;
import com.rohitsuratekar.NCBSinfo.activities.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.activities.experimental.holidays.Holidays;
import com.rohitsuratekar.NCBSinfo.activities.locations.LectureHalls;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.login.Registration;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.reminder.TransportReminder;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentMode;

public class NavigationIDs {

    int resourceID;
    Activity activity;

    public NavigationIDs(int resourceID, Activity activity) {
        this.resourceID = resourceID;
        this.activity = activity;
    }

    public Intent getIntent() {
        switch (resourceID) {
            case R.id.nav_home:
                return getHome();
            case R.id.nav_transport:
                Intent intent = new Intent(activity, Transport.class);
                intent.putExtra(Transport.INDENT, 0);
                return intent;
            case R.id.nav_contacts:
                return new Intent(activity, Contacts.class);
            case R.id.nav_login:
                return new Intent(activity, Login.class);
            case R.id.nav_register:
                return new Intent(activity, Registration.class);
            case R.id.nav_events:
                return new Intent(activity, Events.class);
            case R.id.nav_dashboard:
                return new Intent(activity, DashBoard.class);
            case R.id.nav_experimental:
                return new Intent(activity, Experimental.class);
            case R.id.nav_location:
                return new Intent(activity, LectureHalls.class);
            case R.id.nav_offline_location:
                return new Intent(activity, LectureHalls.class);
            case R.id.nav_canteen:
                return new Intent(activity, Canteen.class);
            case R.id.nav_holidays:
                return new Intent(activity, Holidays.class);
            case R.id.nav_transport_reminder:
                return new Intent(activity, TransportReminder.class);
            default:
                return new Intent(activity, Home.class);
        }
    }


    private Intent getHome() {
        if (new CurrentMode(activity.getBaseContext()).isOffline()) {
            return new Intent(activity, OfflineHome.class);
        } else {
            return new Intent(activity, OnlineHome.class);
        }
    }
}

