package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.Calendar;
import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 19-07-16.
 */
public class BuggyBuilder extends TransportRoute {

    RouteModel model;
    Trips trips;
    Context context;
    Preferences pref;

    public BuggyBuilder(RouteModel model, Context context) {
        this.model = model;
        this.pref = new Preferences(context);
        this.context = context;
        this.trips = new Trips(pref.transport().getWeekdayTrips(model.getRoute()), pref.transport().getSundayTrips(model.getRoute()));
    }

    @Override
    public Routes getRoute() {
        return model.getRoute();
    }

    @Override
    public String getOrigin() {
        return model.getFrom();
    }

    @Override
    public String getDestination() {
        return model.getTo();
    }

    @Override
    public int getRouteNo() {
        return model.getRouteNo();
    }

    @Override
    public Routes.type getRouteType() {
        return model.getType();
    }

    @Override
    public String getType() {
        return context.getResources().getString(R.string.buggy);
    }

    @Override
    public Trips getTrips() {
        return trips;
    }

    @Override
    public String getWeekTitle() {
        return context.getResources().getString(R.string.transport_list_buggy_title_ncbs);
    }

    @Override
    public String getSundayTitle() {
        return context.getResources().getString(R.string.transport_list_buggy_title_mandara);
    }

    @Override
    public String getFooter1() {
        return "";
    }

    @Override
    public String getFooter2() {
        return context.getResources().getString(R.string.transport_buggy_footer,
                new DateConverters().convertToString(Calendar.getInstance(), DateFormats.TIME_12_HOURS_STANDARD));
    }

    @Override
    public LatLng getOriginLocation() {
        return new TransportHelper().getLocation(context, getOrigin(), getRouteType());
    }

    @Override
    public LatLng getDestinationLocation() {
        return new TransportHelper().getLocation(context, getDestination(), getRouteType());
    }


    @Override
    public TransportDynamics getDynamics() {
        return new TransportDynamics(trips);
    }

}
