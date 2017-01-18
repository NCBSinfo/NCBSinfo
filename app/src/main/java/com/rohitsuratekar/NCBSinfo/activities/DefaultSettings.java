package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.RouteMaker;
import com.rohitsuratekar.NCBSinfo.database.RouteManager;
import com.rohitsuratekar.NCBSinfo.database.RouteModel;
import com.rohitsuratekar.NCBSinfo.database.route.Day;
import com.rohitsuratekar.NCBSinfo.database.route.Route;
import com.rohitsuratekar.NCBSinfo.database.route.Trips;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DefaultSettings {

    public List<Route> getDefaultRoutes(Context context) {
        List<Route> allRoutes = new ArrayList<>();

        List<String[]> routeList = new RouteManager(context).getRouteNames();

        for (String[] m : routeList) {
            Route route = new Route();
            route.setOrigin(m[0]);
            route.setDestination(m[1]);
            route.setIcon(getIcon(m[1]));
            List<RouteModel> models = new RouteManager(context).getAllDays(m[0], m[1], m[2]);
            List<Day> everyDay = new ArrayList<>();
            for (RouteModel d : models) {
                everyDay.add(new Day(d.getDay(), d.getTrips()));
                route.setType(d.getType());
                route.setNumber(d.getRoute());
            }
            Trips trips = new Trips(everyDay);
            route.setTrips(trips);
            allRoutes.add(route);
        }

        return allRoutes;
    }

    public static int getIcon(String destination) {
        switch (destination.toLowerCase()) {
            case "ncbs":
                return R.drawable.ncbs;
            case "iisc":
                return R.drawable.iisc;
            case "mandara":
                return R.drawable.mandara;
            case "icts":
                return R.drawable.icts;
            default:
                return R.drawable.unknown;
        }
    }

    public void buildDefaultRoutes(Context context) {
        RouteManager manager = new RouteManager(context);
        manager.add(new RouteMaker(context).make("ncbs", "mandara", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_buggy_from_ncbs)), "buggy"));
       /* manager.add(new RouteMaker(context).make("mandara", "ncbs", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_buggy_from_mandara)), "buggy"));


        manager.add(new RouteMaker(context).make("ncbs", "iisc", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_ncbs_iisc_sunday)), "shuttle"));
        manager.add(new RouteMaker(context).make("ncbs", "iisc", Calendar.MONDAY,
                convertToList(context.getString(R.string.def_ncbs_iisc_week)), "shuttle"));*/

        manager.add(new RouteMaker(context).make("iisc", "ncbs", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_iisc_ncbs_sunday)), "shuttle"));
        manager.add(new RouteMaker(context).make("iisc", "ncbs", Calendar.MONDAY,
                convertToList(context.getString(R.string.def_iisc_ncbs_week)), "shuttle"));

        manager.add(new RouteMaker(context).make("ncbs", "mandara", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_ncbs_mandara_sunday)), "shuttle"));
        manager.add(new RouteMaker(context).make("ncbs", "mandara", Calendar.MONDAY,
                convertToList(context.getString(R.string.def_ncbs_mandara_week)), "shuttle"));

        manager.add(new RouteMaker(context).make("mandara", "ncbs", Calendar.MONDAY,
                convertToList(context.getString(R.string.def_mandara_ncbs_week)), "shuttle"));
        manager.add(new RouteMaker(context).make("mandara", "ncbs", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_mandara_ncbs_sunday)), "shuttle"));

        manager.add(new RouteMaker(context).make("ncbs", "cbl", Calendar.SUNDAY,
                convertToList(context.getString(R.string.def_ncbs_cbl)), "TT"));

    }

    private static List<String> convertToList(String s) {
        List<String> list = new ArrayList<>();
        String[] arr = s.replace("{", "").replace("}", "").split(",");
        for (String m : arr) {
            list.add(m.trim());
        }
        return list;
    }

}
