package com.rohitsuratekar.NCBSinfo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.interfaces.User;

/**
 * Custom navigation drawer
 * Use this class to change your dynamic UI elements of Navigation Drawer.
 * Use 'set' function to set according to your mode
 * Create different functions for any new mode
 */

public class CurrentNavigationDrawer implements User {

    CurrentActivity currentActivity;
    NavigationView navigationView;
    SharedPreferences pref;
    String versionName;
    Context context;
    View header;
    TextView name, email;

    public CurrentNavigationDrawer(CurrentActivity currentActivity, NavigationView navigationView, Context context) {
        this.currentActivity = currentActivity;
        this.navigationView = navigationView;
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void set() {
        navigationView.inflateMenu(R.menu.base_drawer); //Drawer background
        navigationView.setCheckedItem(currentActivity.getDrawerItem()); //Selected item

        if (pref.getString(MODE, OFFLINE).equals(OFFLINE)) {
            navigationView.inflateHeaderView(R.layout.nav_header_offline); //Header
            setCommon();
            setOffline();
        } else {
            navigationView.inflateHeaderView(R.layout.nav_header); //Header
            setCommon();
            setOnline();
        }

    }

    private void setCommon() {
        header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.nav_header_name);
        email = (TextView) header.findViewById(R.id.nav_header_email);
    }


    private void setOffline() {
        if (name != null) {
            name.setText(context.getString(R.string.app_name));
            email.setText(context.getString(R.string.app_version, versionName));
        }
        navigationView.getMenu().removeGroup(R.id.nav_upper_group);
        navigationView.getMenu().removeItem(R.id.nav_updates);
        navigationView.getMenu().removeItem(R.id.nav_experimental);
        navigationView.getMenu().findItem(R.id.nav_change_mode).setIcon(R.drawable.icon_wifi_off);

    }

    private void setOnline() {
        MenuItem currentMenu = navigationView.getMenu().findItem(R.id.nav_dashboard);
        if (currentMenu != null) {
            currentMenu.setTitle(pref.getString(profile.NAME, "User").trim().split(" ")[0] + "\'s " + context.getString(R.string.dashboard));
        }
        if (name != null) {
            name.setText(pref.getString(profile.NAME, "User Name"));
            email.setText(pref.getString(profile.EMAIL, "email@domain.com"));
        }
        navigationView.getMenu().removeItem(R.id.nav_offline_location);
        navigationView.getMenu().findItem(R.id.nav_change_mode).setIcon(R.drawable.icon_wifi_on);
    }
}
