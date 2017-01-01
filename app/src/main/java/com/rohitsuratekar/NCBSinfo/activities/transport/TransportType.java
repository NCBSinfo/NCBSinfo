package com.rohitsuratekar.NCBSinfo.activities.transport;

import com.rohitsuratekar.NCBSinfo.R;

public enum TransportType {

    SHUTTLE(R.string.shuttle, R.drawable.icon_shuttle),
    BUGGY(R.string.buggy, R.drawable.icon_buggy),
    MINI_SHUTTLE(R.string.shuttle, R.drawable.icon_shuttle);

    private int name;
    private int icon;

    TransportType(int name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
