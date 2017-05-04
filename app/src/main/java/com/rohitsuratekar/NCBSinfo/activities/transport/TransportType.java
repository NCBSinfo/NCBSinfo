package com.rohitsuratekar.NCBSinfo.activities.transport;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public enum TransportType {

    SHUTTLE(R.string.shuttle, 30, R.drawable.icon_transport, R.color.colorPrimary),
    BUGGY(R.string.buggy, 12, R.drawable.icon_home, R.color.colorPrimary),
    TTC(R.string.ttc, 16, R.drawable.icon_transport, R.color.colorPrimary),
    UNKNOWN(R.string.unknown, 0, R.drawable.icon_transport, R.color.colorPrimary);

    private int name;
    private int seats;
    private int icon;
    private int color;

    TransportType(int name, int seats, int icon, int color) {
        this.name = name;
        this.seats = seats;
        this.icon = icon;
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public int getSeats() {
        return seats;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }
}
