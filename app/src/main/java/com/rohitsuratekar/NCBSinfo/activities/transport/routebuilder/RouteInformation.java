package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;

/**
 * All information needed to make Routes should set from this helper class
 */
public class RouteInformation {
    int routeNo;

    public RouteInformation(Routes routes) {
        this.routeNo = routes.getRouteNo();
    }

    public RouteModel get() {

        RouteModel model = new RouteModel();
        model.setFrom("ncbs");
        model.setTo("iisc");
        model.setRouteNo(routeNo);
        model.setWeekDayKey("r0_week");
        model.setSundayKey("r0_sunday");
        model.setType(Routes.type.SHUTTLE);

        if (routeNo == Routes.NCBS_IISC.getRouteNo()) {
            model.setFrom("ncbs");
            model.setTo("iisc");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r0_week");
            model.setSundayKey("r0_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.IISC_NCBS.getRouteNo()) {
            model.setFrom("iisc");
            model.setTo("ncbs");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r1_week");
            model.setSundayKey("r1_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.NCBS_MANDARA.getRouteNo()) {
            model.setFrom("ncbs");
            model.setTo("mandara");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r2_week");
            model.setSundayKey("r2_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.MANDARA_NCBS.getRouteNo()) {
            model.setFrom("mandara");
            model.setTo("ncbs");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r3_week");
            model.setSundayKey("r3_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.BUGGY_FROM_NCBS.getRouteNo()) {
            model.setFrom("ncbs");
            model.setTo("mandara");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r4_common");
            model.setSundayKey("r4_common");
            model.setType(Routes.type.BUGGY);
        } else if (routeNo == Routes.BUGGY_FROM_MANDARA.getRouteNo()) {
            model.setFrom("mandara");
            model.setTo("ncbs");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r5_common");
            model.setSundayKey("r5_common");
            model.setType(Routes.type.BUGGY);
        } else if (routeNo == Routes.NCBS_ICTS.getRouteNo()) {
            model.setFrom("ncbs");
            model.setTo("icts");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r6_week");
            model.setSundayKey("r6_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.ICTS_NCBS.getRouteNo()) {
            model.setFrom("icts");
            model.setTo("ncbs");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r7_week");
            model.setSundayKey("r7_sunday");
            model.setType(Routes.type.SHUTTLE);
        } else if (routeNo == Routes.NCBS_CBL.getRouteNo()) {
            model.setFrom("ncbs");
            model.setTo("cbl");
            model.setRouteNo(routeNo);
            model.setWeekDayKey("r8_common");
            model.setSundayKey("r8_common");
            model.setType(Routes.type.SHUTTLE);
        }

        return model;
    }
}
