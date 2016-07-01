package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransportModel {

    Routes currentRoute;
    SharedPreferences pref;
    Context context;
    String getWeekTitle;
    String getSundayTitle;
    String getFootnote1;
    String getFootnote2;
    String type;
    LatLng originLocation;
    LatLng destinationLocation;
    String[] rawTripsWeekDays;
    String[] rawTripsSunday;
    String nextTrip;
    int nextTripDay;

    public TransportModel(Routes currentRoute, Context context) {
        this.currentRoute = currentRoute;
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        if (currentRoute.isBuggy()) {
            setUpBuggy(currentTime);
        } else {
            setUpShuttle(currentTime);
        }
        this.originLocation = new TransportHelper(context).getLocation(context, currentRoute.getFrom(), false);
        this.destinationLocation = new TransportHelper(context).getLocation(context, currentRoute.getTo(), false);
        this.rawTripsWeekDays = new Converters().stringToarray(pref.getString(currentRoute.getWeekKey(), TransportHelper.DEFAULT_TRIPS));
        this.rawTripsSunday = new Converters().stringToarray(pref.getString(currentRoute.getSundayKey(), TransportHelper.DEFAULT_TRIPS));

        String[] t = new TransportHelper(context).nextTrip(currentRoute);
        this.nextTripDay = Integer.parseInt(t[0]);
        this.nextTrip = t[1];

    }

    public String getFrom() {
        return currentRoute.getFrom();
    }

    public String getTO() {
        return currentRoute.getTo();
    }

    public int getRouteNo() {
        return currentRoute.getRouteNo();
    }

    public boolean isBuggy() {
        return currentRoute.isBuggy();
    }

    public String getGetWeekTitle() {
        return getWeekTitle;
    }

    public String getGetSundayTitle() {
        return getSundayTitle;
    }

    public String getGetFootnote1() {
        return getFootnote1;
    }

    public String getGetFootnote2() {
        return getFootnote2;
    }

    public String getType() {
        return type;
    }

    public LatLng getOriginLocation() {
        return originLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public String[] getRawTripsWeekDays() {
        return rawTripsWeekDays;
    }

    public String[] getRawTripsSunday() {
        return rawTripsSunday;
    }

    public String getNextTrip() {
        return nextTrip;
    }

    public int getNextTripDay() {
        return nextTripDay;
    }

    private void setUpShuttle(String currentTime) {

        type = context.getResources().getString(R.string.shuttle);
        getWeekTitle = context.getResources().getString(R.string.transport_list_week_title);
        getSundayTitle = context.getResources().getString(R.string.transport_list_sunday_title);
        getFootnote1 = context.getResources().getString(R.string.transport_footer1);
        getFootnote2 = context.getResources().getString(R.string.transport_footer2, currentTime);

    }

    private void setUpBuggy(String currentTime) {
        type = context.getResources().getString(R.string.buggy);
        getWeekTitle = context.getResources().getString(R.string.transport_list_buggy_title_ncbs);
        getSundayTitle = context.getResources().getString(R.string.transport_list_buggy_title_mandara);
        getFootnote1 = "";
        getFootnote2 = context.getResources().getString(R.string.transport_buggy_footer, currentTime);

    }

}
