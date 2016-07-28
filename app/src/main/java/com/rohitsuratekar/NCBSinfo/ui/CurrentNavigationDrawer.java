package com.rohitsuratekar.NCBSinfo.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentMode;

/**
 * Custom navigation drawer
 * Use this class to change your dynamic UI elements of Navigation Drawer.
 * Use 'set' function to set according to your mode
 * Create different functions for any new mode
 */

public class CurrentNavigationDrawer {

    CurrentActivity currentActivity;
    NavigationView navigationView;
    Preferences pref;
    String versionName;
    Context context;
    View header;
    TextView name, email;
    CurrentMode mode;


    public CurrentNavigationDrawer(CurrentActivity currentActivity, NavigationView navigationView, Context context) {
        this.currentActivity = currentActivity;
        this.navigationView = navigationView;
        this.context = context;
        this.pref = new Preferences(context);
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.mode = new CurrentMode(context);
    }

    //Call this activity from outside class
    public void set() {
        navigationView.inflateMenu(R.menu.base_drawer); //Drawer background
        navigationView.setCheckedItem(currentActivity.getDrawerItem()); //Selected item

        if (mode.isOffline()) {
            navigationView.inflateHeaderView(R.layout.nav_header_offline); //Header
            setCommon();
            setOffline();
        } else {
            navigationView.inflateHeaderView(R.layout.nav_header); //Header
            setCommon();
            setOnline();
        }

        //Set subgroups
        setSubgroups();

    }

    //Common UI elements and hide all subgroups
    private void setCommon() {
        header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.nav_header_name);
        email = (TextView) header.findViewById(R.id.nav_header_email);
        navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_experimental, false);
        navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_login, false);
        navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_transport, false);
    }

    //Offline mode
    private void setOffline() {
        if (name != null) {
            name.setText(context.getString(R.string.app_name));
            email.setText(context.getString(R.string.app_version, versionName));
        }
        navigationView.getMenu().removeGroup(R.id.nav_upper_group);
        navigationView.getMenu().removeItem(R.id.nav_events);
        navigationView.getMenu().removeItem(R.id.nav_experimental);
        navigationView.getMenu().findItem(R.id.nav_change_mode).setIcon(R.drawable.icon_wifi_off);

    }

    //Online mode
    private void setOnline() {
        MenuItem currentMenu = navigationView.getMenu().findItem(R.id.nav_dashboard);
        if (currentMenu != null) {
            currentMenu.setTitle(pref.user().getName().trim().split(" ")[0] + "\'s " + context.getString(R.string.dashboard));
        }
        if (name != null) {
            name.setText(pref.user().getName());
            email.setText(pref.user().getEmail());
        }
        navigationView.getMenu().removeItem(R.id.nav_offline_location);
        navigationView.getMenu().findItem(R.id.nav_change_mode).setIcon(R.drawable.icon_wifi_on);
    }

    private void setSubgroups() {
        switch (currentActivity) {
            case HOME:
                navigationView.getMenu().setGroupEnabled(R.id.nav_main_group, false);
                break;
            case TRANSPORT:
                if (!mode.isOffline()) {
                    navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_transport, true);
                }
                break;
            case EXPERIMENTAL:
                navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_experimental, true);
                break;
            case LOGIN:
                setOffline();
                navigationView.getMenu().setGroupEnabled(R.id.nav_main_group, false);
                navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_login, true);
                break;
            case REGISTRATION:
                setOffline();
                navigationView.getMenu().setGroupEnabled(R.id.nav_main_group, false);
                navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_login, true);
                break;
            case LECTUREHALLS:
                if (pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    navigationView.getMenu().findItem(R.id.nav_offline_location).setChecked(true);
                } else {
                    navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_experimental, true);
                    navigationView.getMenu().findItem(R.id.nav_location).setChecked(true);
                }
                break;
            case CANTEEN:
                navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_experimental, true);
                break;
            case HOLIDAYS:
                navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_experimental, true);
                break;
            case TRANSPORT_REMINDER:
                if (!mode.isOffline()) {
                    navigationView.getMenu().setGroupVisible(R.id.nav_subgroup_transport, true);
                }
                break;

        }
    }
}
