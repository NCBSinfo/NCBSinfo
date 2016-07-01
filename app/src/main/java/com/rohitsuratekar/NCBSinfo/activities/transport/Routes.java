package com.rohitsuratekar.NCBSinfo.activities.transport;

public enum Routes {


    NCBS_IISC(0, false, "r0_week", "r0_sunday", "ncbs", "iisc"),
    IISC_NCBS(1, false, "r1_week", "r1_sunday", "iisc", "ncbs"),
    NCBS_MANDARA(2, false, "r2_week", "r2_sunday", "ncbs", "mandara"),
    MANDARA_NCBS(3, false, "r3_week", "r3_sunday", "mandara", "ncbs"),
    BUGGY_FROM_NCBS(4, true, "r4_common", "r4_common", "ncbs", "mandara"),
    BUGGY_FROM_MANDARA(5, true, "r5_common", "r5_common", "mandara", "ncbs"),
    NCBS_ICTS(6, false, "r6_week", "r6_sunday", "ncbs", "icts"),
    ICTS_NCBS(7, false, "r7_week", "r7_sunday", "icts", "ncbs"),
    NCBS_CBL(8, false, "r8_common", "r8_common", "ncbs", "cbl");


    private final int routeNo;
    private final boolean isBuggy;
    private final String weekKey;
    private final String sundayKey;
    private final String from;
    private final String to;

    /**
     * @param routeNo   : Unique route number
     * @param isBuggy   : Is this buggy ?
     * @param weekKey   : Sharedpreference key for week day trips
     * @param sundayKey : Sharedpreference key for Sunday day trips
     * @param from      : String returns origin of trip
     * @param to        : String returns destination of trip
     */

    Routes(int routeNo, boolean isBuggy, String weekKey, String sundayKey, String from, String to) {
        this.routeNo = routeNo;
        this.isBuggy = isBuggy;
        this.weekKey = weekKey;
        this.sundayKey = sundayKey;
        this.from = from;
        this.to = to;
    }

    public int getRouteNo() {
        return routeNo;
    }

    public boolean isBuggy() {
        return isBuggy;
    }

    public String getWeekKey() {
        return weekKey;
    }

    public String getSundayKey() {
        return sundayKey;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
