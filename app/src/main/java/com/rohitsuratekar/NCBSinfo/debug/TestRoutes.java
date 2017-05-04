package com.rohitsuratekar.NCBSinfo.debug;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.DayTrips;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class TestRoutes {

    public List<Route> getAllRoutes() {
        List<Route> all = new ArrayList<>();
        Route r1 = new Route();
        r1.setOrigin("NCBS");
        r1.setDestination("IISC");
        r1.setType(TransportType.SHUTTLE);
        r1.setColor(R.color.colorPrimary);
        r1.setColorDark(R.color.colorPrimaryDark);
        r1.setDayTrips(getTrips());
        all.add(r1);
        Route r2 = new Route();
        r2.setOrigin("MANDARA");
        r2.setDestination("NCBS");
        r2.setType(TransportType.BUGGY);
        r2.setColor(R.color.md_teal_500);
        r2.setColorDark(R.color.md_teal_700);
        r2.setDayTrips(getTrips());
        all.add(r2);
        Route r3 = new Route();
        r3.setOrigin("ICTS");
        r3.setDestination("SOME TEST");
        r3.setType(TransportType.BUGGY);
        r3.setColor(R.color.colorPrimary);
        r3.setColorDark(R.color.colorPrimaryDark);
        r3.setDayTrips(getTrips());
        all.add(r3);
        return all;
    }

    private List<DayTrips> getTrips() {

        List<DayTrips> models = new ArrayList<>();

        DayTrips model = new DayTrips();
        List<String> tripList = new ArrayList<>();
        tripList.add("08:30");
        tripList.add("11:00");
        tripList.add("12:00");
        tripList.add("16:00");
        tripList.add("17:00");
        tripList.add("01:15");
        model.setTripsStrings(tripList);
        model.setDay(5);

        models.add(model);

        DayTrips model2 = new DayTrips();
        List<String> tripList2 = new ArrayList<>();
        tripList2.add("07:00");
        tripList2.add("12:00");
        tripList2.add("16:00");
        tripList2.add("00:00");
        model2.setTripsStrings(tripList2);
        model2.setDay(6);

        models.add(model2);

        return models;
    }
}
