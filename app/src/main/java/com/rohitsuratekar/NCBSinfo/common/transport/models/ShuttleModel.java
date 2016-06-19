package com.rohitsuratekar.NCBSinfo.common.transport.models;


import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;

import java.util.List;

public class ShuttleModel {
    int routeNo;
    String from;
    String to;
    SundayModel sundayTrips;
    MondayModel mondayTrips;
    WeekDayModel weekdayTrips;
    SundayModel rawSunday;
    WeekDayModel rawWeekday;
    String[] nextTrip;

    public ShuttleModel(int routeNo) {
        this.routeNo = routeNo;
        this.from = new TransportHelper().routeToStrings(routeNo)[0];
        this.to = new TransportHelper().routeToStrings(routeNo)[1];
    }

    public ShuttleModel(int routeNo, SundayModel rawSunday, WeekDayModel rawWeekday) {
        this.routeNo = routeNo;
        this.rawSunday = rawSunday;
        this.rawWeekday = rawWeekday;
        this.from = new TransportHelper().routeToStrings(routeNo)[0];
        this.to = new TransportHelper().routeToStrings(routeNo)[1];
        List<List<String>> allModels = new TransportHelper().rawToRegular(rawSunday.getTrips(),rawWeekday.getTrips());
        this.sundayTrips = new SundayModel(allModels.get(0));
        this.mondayTrips = new MondayModel(allModels.get(1));
        this.weekdayTrips = new WeekDayModel(allModels.get(2));
        this.nextTrip = new TransportHelper().nextTrip(sundayTrips,mondayTrips,weekdayTrips);

    }

    public int getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(int routeNo) {
        this.routeNo = routeNo;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }


    public SundayModel getSundayTrips() {
        return sundayTrips;
    }


    public MondayModel getMondayTrips() {
        return mondayTrips;
    }


    public WeekDayModel getWeekdayTrips() {
        return weekdayTrips;
    }


    public SundayModel getRawSunday() {
        return rawSunday;
    }

    public void setRawSunday(SundayModel rawSunday) {
        this.rawSunday = rawSunday;
    }

    public WeekDayModel getRawWeekday() {
        return rawWeekday;
    }

    public void setRawWeekday(WeekDayModel rawWeekday) {
        this.rawWeekday = rawWeekday;
    }

    public String[] getNextTrip() {
        return nextTrip;
    }
}
