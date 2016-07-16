package com.rohitsuratekar.NCBSinfo.constants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 09-07-16.
 */
public enum DateFormats {

    TIMESTAMP_STANDARD("hh:mm:ss a dd MMM yy", true, true),
    DATE_STANDARD("dd/MM/yyyy", true, false),
    TIME_24_HOURS_STANDARD("HH:mm", false, true),
    TIME_12_HOURS_STANDARD("hh:mm a", false, true),
    ONLINE_STANDARD("dd/MM/yyyy HH:mm:ss", true, true),
    TRANSPORT_FORMAT("dd/MM/yyyy HH:mm", true, true),
    READABLE_DATE("dd MMM yyyy", true, false);


    private final String format;
    private final boolean isDateComplete;
    private final boolean isTimeComplete;

    /**
     * @param format         : String format
     * @param isDateComplete : Is date given in format? If not, it will use curent date
     * @param isTimeComplete Is time given in format? If not, it will use current time
     */

    DateFormats(String format, boolean isDateComplete, boolean isTimeComplete) {
        this.format = format;
        this.isDateComplete = isDateComplete;
        this.isTimeComplete = isTimeComplete;
    }

    public String getFormat() {
        return format;
    }

    public boolean isDateComplete() {
        return isDateComplete;
    }

    public boolean isTimeComplete() {
        return isTimeComplete;
    }
}
