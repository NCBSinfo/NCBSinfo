package com.rohitsuratekar.NCBSinfo.ui;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.contact.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Dashboard;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Notifications;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.locations.Locations;
import com.rohitsuratekar.NCBSinfo.activities.login.AccountSecurity;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.login.Register;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;

public enum CurrentActivity {

    HOME(Home.class, R.layout.home, R.string.home, R.id.nav_home, R.menu.base_menu),
    TRANSPORT(Transport.class, R.layout.transport, R.string.transport, R.id.nav_transport, R.menu.transport_menu),
    CONTACTS(Contacts.class, R.layout.contacts, R.string.contacts, R.id.nav_contacts, R.menu.contacts_menu),
    LOCATIONS(Locations.class, R.layout.locations, R.string.locations, R.id.nav_locations, R.menu.base_menu),
    LOGIN(Login.class, R.layout.login, R.string.log_in, R.id.nav_header_option, R.menu.blank_menu),
    REGISTER(Register.class, R.layout.register, R.string.register, R.id.nav_header_option, R.menu.blank_menu),
    NOTIFICATIONS(Notifications.class, R.layout.notifications, R.string.notifications, R.id.nav_header_option, R.menu.blank_menu),
    DASHBOARD(Dashboard.class, R.layout.dashboard, R.string.dashboard, R.id.nav_dashboard, R.menu.blank_menu),
    SECURITY(AccountSecurity.class, R.layout.account_security, R.string.security, R.id.nav_header_option, R.menu.blank_menu);

    private Class currentClass;
    private int layout;
    private int name;
    private int navigationMenu;
    private int menu;

    CurrentActivity(Class currentClass, int layout, int name, int navigationMenu, int menu) {
        this.currentClass = currentClass;
        this.layout = layout;
        this.name = name;
        this.navigationMenu = navigationMenu;
        this.menu = menu;
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

    public int getMenu() {
        return menu;
    }
}
