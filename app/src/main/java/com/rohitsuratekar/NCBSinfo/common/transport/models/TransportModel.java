package com.rohitsuratekar.NCBSinfo.common.transport.models;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransportModel {
    ShuttleModel shuttle;
    BuggyModel buggy;
    String weekTitle;
    String sundayTitle;
    String footnote1;
    String footnote2;
    Context context;
    String[] rawTripsWeekDays;
    String[] rawTripsSunday;

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
}
