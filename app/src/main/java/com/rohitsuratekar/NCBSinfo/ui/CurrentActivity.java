package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;

public enum CurrentActivity {

    HOME(Home.class, R.layout.home, R.string.home, R.id.nav_home, false),
    TRANSPORT(Transport.class, R.layout.transport, R.string.transport, R.id.nav_transport, false);

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
