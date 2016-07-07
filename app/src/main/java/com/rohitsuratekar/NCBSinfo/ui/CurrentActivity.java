package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * This enum will decide activity UI
 * Check constructors for inputs
 */
public enum CurrentActivity {

    HOME(
            R.string.app_name,
            R.layout.home,
            R.id.nav_home,
            false), //Main Home and Launcher activity

    ONLINE_HOME(
            R.string.app_name,
            R.layout.online_home,
            R.id.nav_home,
            false), //Online Home activity

    OFFLINE_HOME(
            R.string.app_name,
            R.layout.offline_home,
            R.id.nav_home,
            false), //Online Home activity

    TRANSPORT(
            R.string.transport,
            R.layout.base_viewpager,
            R.id.nav_transport,
            true), //Transport

    CONTACTS(
            R.string.contacts,
            R.layout.base_viewpager,
            R.id.nav_contacts,
            true), //Contacts

    CONTACTS_ADD(
            R.string.contacts,
            R.layout.contact_add,
            R.id.nav_contacts,
            false), //Add contacts

    LOGIN(
            R.string.login,
            R.layout.login,
            R.id.nav_login,
            false), //Login

    REGISTRATION(
            R.string.register,
            R.layout.register,
            R.id.nav_register,
            false), //Registration

    EVENTS(
            R.string.event_updates,
            R.layout.base_viewpager,
            R.id.nav_events,
            true), //Events

    DASHBOARD(
            R.string.dashboard,
            R.layout.dashboard,
            R.id.nav_dashboard,
            false), //Dashboard

    EXPERIMENTAL(
            R.string.experimental,
            R.layout.experimental,
            R.id.nav_experimental,
            false),

    LECTUREHALLS(
            R.string.lecture_hall,
            R.layout.lecturehalls,
            R.id.nav_location,
            false),

    CANTEEN(
            R.string.canteen,
            R.layout.canteen,
            R.id.nav_canteen,
            false),

    HOLIDAYS(
            R.string.holidays,
            R.layout.base_viewpager,
            R.id.nav_holidays,
            true),

    TRANSPORT_REMINDER(
            R.string.transport_reminder,
            R.layout.base_viewpager,
            R.id.nav_transport_reminder,
            true);


    private final int Title;
    private final int Layout;
    private final int DrawerItem;
    private final boolean needTabLayout;

    /**
     * Need this for setting up UI fo respective activity
     *
     * @param Title         : Name of activity
     * @param Layout        : Layout resource of activity
     * @param DrawerItem    : ID of navigation item which will be highlighted
     * @param needTabLayout : Do this activity need tab host for fragment hosting
     */

    CurrentActivity(int Title, int Layout, int DrawerItem, boolean needTabLayout) {
        this.Title = Title;
        this.Layout = Layout;
        this.DrawerItem = DrawerItem;
        this.needTabLayout = needTabLayout;
    }

    public int getTitle() {
        return Title;
    }

    public int getLayout() {
        return Layout;
    }

    public int getDrawerItem() {
        return DrawerItem;
    }

    public boolean needTabLayout() {
        return needTabLayout;
    }
}
