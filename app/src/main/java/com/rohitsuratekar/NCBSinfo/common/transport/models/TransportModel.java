package com.rohitsuratekar.NCBSinfo.common.transport.models;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransportModel {
    ShuttleModel shuttle;
    BuggyModel buggy;
    String weekTitle;
    String sundayTitle;
    String footnote1, footnote2;
    String from, to;
    Context context;
    String[] rawTripsWeekDays;
    String[] rawTripsSunday;
    LatLng originLocation;
    LatLng destinationLocation;
    String nextTrip;
    String timeLeft[];
    int routeNo;
    int nextTripDay;
    String type;


    public TransportModel(Context context, ShuttleModel shuttle) {
        SimpleDateFormat modformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = modformat.format(Calendar.getInstance().getTime());
        this.context = context;
        this.shuttle = shuttle;
        this.rawTripsWeekDays = shuttle.getRawWeekday().getTrips()
                .toArray(new String[shuttle.getRawWeekday().getTrips().size()]);
        this.rawTripsSunday = shuttle.getRawSunday().getTrips()
                .toArray(new String[shuttle.getRawSunday().getTrips().size()]);
        this.weekTitle = context.getResources().getString(R.string.transport_list_week_title);
        this.sundayTitle = context.getResources().getString(R.string.transport_list_sunday_title);
        this.footnote1 = context.getResources().getString(R.string.transport_footer1);
        this.footnote2 = context.getResources().getString(R.string.transport_footer2, currentTime);
        this.originLocation = new TransportHelper().getLocation(context, shuttle.from, false);
        this.destinationLocation = new TransportHelper().getLocation(context, shuttle.to, false);
        this.nextTrip = shuttle.getNextTrip()[1];
        this.nextTripDay = Integer.parseInt(shuttle.getNextTrip()[0]);
        this.routeNo = shuttle.getRouteNo();
        this.from = shuttle.getFrom();
        this.to = shuttle.getTo();
        this.type = context.getResources().getString(R.string.shuttle);
    }

    public TransportModel(Context context, BuggyModel buggy) {
        SimpleDateFormat modformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = modformat.format(Calendar.getInstance().getTime());
        this.context = context;
        this.buggy = buggy;
        this.rawTripsWeekDays = buggy.getTripsFromNCBS();
        this.rawTripsSunday = buggy.getTripsFromMandara();
        this.weekTitle = context.getResources().getString(R.string.transport_list_buggy_title_ncbs);
        this.sundayTitle = context.getResources().getString(R.string.transport_list_buggy_title_mandara);
        this.footnote1 = "";
        this.footnote2 = context.getResources().getString(R.string.transport_buggy_footer, currentTime);
        if (buggy.from != null) {
            this.from = buggy.from;
            this.to = buggy.to;
            this.originLocation = new TransportHelper().getLocation(context,buggy.from,true);
            this.destinationLocation = new TransportHelper().getLocation(context,buggy.to,true);
            this.routeNo = buggy.getRouteNo();
            if (buggy.from.equals("ncbs")) {
                this.nextTrip = buggy.getNextTrip()[0];
                this.nextTripDay = Integer.parseInt(buggy.getNextTrip()[2]);
            } else {
                this.nextTrip = buggy.getNextTrip()[1];
                this.nextTripDay = Integer.parseInt(buggy.getNextTrip()[3]);
            }
        }
        this.type = context.getResources().getString(R.string.buggy);
    }

    public int getNextTripDay() {
        return nextTripDay;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public ShuttleModel getShuttle() {
        return shuttle;
    }

    public void setShuttle(ShuttleModel shuttle) {
        this.shuttle = shuttle;
    }

    public BuggyModel getBuggy() {
        return buggy;
    }

    public void setBuggy(BuggyModel buggy) {
        this.buggy = buggy;
    }

    public String getWeekTitle() {
        return weekTitle;
    }

    public String getSundayTitle() {
        return sundayTitle;
    }

    public String getFootnote1() {
        return footnote1;
    }

    public String getFootnote2() {
        return footnote2;
    }

    public Context getContext() {
        return context;
    }

    public String[] getRawTripsWeekDays() {
        return rawTripsWeekDays;
    }

    public String[] getRawTripsSunday() {
        return rawTripsSunday;
    }

    public LatLng getOriginLocation() {
        return originLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public String getNextTrip() {
        return nextTrip;
    }

    public int getRouteNo() {
        return routeNo;
    }

    public float[] getTimeLeft() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat shortformat = new SimpleDateFormat("MM/dd/yyyy ", Locale.getDefault());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, nextTripDay);
        String now = format.format(date);
        date.setTime(calendar.getTimeInMillis());
        String target = null;
        try {
            target = format.format(format.parse(shortformat.format(date) + nextTrip + ":00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new TransportHelper().TimeLeft(now, target);
    }

}
