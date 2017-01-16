package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Home;
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.locations.Locations;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;

public enum CurrentActivity {

    HOME(Home.class, R.layout.home, R.string.home, R.id.nav_home, false),
    TRANSPORT(Transport.class, R.layout.transport, R.string.transport, R.id.nav_transport, false),
    TRANSPORT_EDIT(TransportEdit.class, R.layout.transport_edit, R.string.transport_edit, R.id.nav_transport_edit, false),
    CONTACTS(Contacts.class, R.layout.contacts, R.string.contacts, R.id.nav_contacts, false),
    LOCATIONS(Locations.class, R.layout.locations, R.string.locations, R.id.nav_locations, false);

    private Class currentClass;
    private int layout;
    private int name;
    private int navigationMenu;
    private boolean isTabEnabled;

    CurrentActivity(Class currentClass, int layout, int name, int navigationMenu, boolean isTabEnabled) {
        this.currentClass = currentClass;
        this.layout = layout;
        this.name = name;
        this.navigationMenu = navigationMenu;
        this.isTabEnabled = isTabEnabled;
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public int getLayout() {
        return layout;
    }

    public int getName() {
        return name;
    }

    public int getNavigationMenu() {
        return navigationMenu;
    }

    public boolean isTabEnabled() {
        return isTabEnabled;
    }
}
