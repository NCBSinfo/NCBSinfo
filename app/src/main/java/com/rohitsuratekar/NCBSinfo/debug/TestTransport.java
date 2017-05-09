package com.rohitsuratekar.NCBSinfo.debug;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class TestTransport {

    private List<String> getTrips() {
        List<List<String>> days = new ArrayList<>();

        List<String> r1 = new ArrayList<>();
        r1.add("07:30");
        r1.add("09:40");
        r1.add("12:00");
        r1.add("18:00");

        days.add(r1);

        List<String> r2 = new ArrayList<>();
        r2.add("08:30");
        r2.add("09:00");
        r2.add("12:00");
        r2.add("13:15");
        r2.add("16:15");
        r2.add("20:15");

        days.add(r2);

        return days.get(General.randInt(0, days.size() - 1));
    }

    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();

        Route r1 = new Route();
        r1.setOrigin("NCBS");
        r1.setDestination("Mandara");
        r1.setType(TransportType.SHUTTLE);
        r1.addDay(Calendar.SUNDAY, getTrips());
        r1.addDay(Calendar.MONDAY, getTrips());
        routes.add(r1);

        Route r2 = new Route();
        r2.setOrigin("ITCS");
        r2.setDestination("Mandara");
        r2.setType(TransportType.BUGGY);
        r2.addDay(Calendar.MONDAY, getTrips());
        routes.add(r2);

        Route r3 = new Route();
        r3.setOrigin("IISc");
        r3.setDestination("NCBS");
        r3.setType(TransportType.SHUTTLE);
        r3.addDay(Calendar.SUNDAY, getTrips());
        r3.addDay(Calendar.MONDAY, getTrips());
        routes.add(r3);

        return routes;

    }
}
