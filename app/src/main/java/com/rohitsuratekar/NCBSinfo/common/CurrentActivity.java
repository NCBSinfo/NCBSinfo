package com.rohitsuratekar.NCBSinfo.common;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public enum CurrentActivity {

    HOME(Home.class, R.string.home, R.layout.home, R.id.nav_home),
    TRANSPORT(Transport.class, R.string.transport, R.layout.transport, R.id.nav_transport);

    private Class aClass;
    private int name;
    private int layout;
    private int navigationID;

    CurrentActivity(Class aClass, int name, int layout, int navigationID) {
        this.aClass = aClass;
        this.name = name;
        this.layout = layout;
        this.navigationID = navigationID;
    }

    public Class getaClass() {
        return aClass;
    }

    public int getName() {
        return name;
    }

    public int getLayout() {
        return layout;
    }

    public int getNavigationID() {
        return navigationID;
    }
}
