package com.rohitsuratekar.NCBSinfo.activities.transport;

public enum Routes {

    NCBS_IISC(0),
    IISC_NCBS(1),
    NCBS_MANDARA(2),
    MANDARA_NCBS(3),
    BUGGY_FROM_NCBS(4),
    BUGGY_FROM_MANDARA(5),
    NCBS_ICTS(6),
    ICTS_NCBS(7),
    NCBS_CBL(8);

    private int routeNo;

    Routes(int routeNo) {
        this.routeNo = routeNo;
    }

    public int getRouteNo() {
        return routeNo;
    }

    public enum type {
        SHUTTLE,
        BUGGY
    }
}
