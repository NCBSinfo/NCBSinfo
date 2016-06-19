package com.rohitsuratekar.NCBSinfo.common.transport.models;

import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;

import java.util.Arrays;
import java.util.Calendar;

public class BuggyModel {

    String[] tripsFromNCBS;
    String[] tripsFromMandara;
    String[] nextTrip;
    String from;
    String to;
    int routeNo;

    public BuggyModel(String[] tripsFromNCBS, String[] tripsFromMandara) {
        this.tripsFromNCBS = tripsFromNCBS;
        this.tripsFromMandara = tripsFromMandara;
        this.nextTrip = nextTrip();
        this.from = "ncbs";
        this.from = "mandara";
    }

    public BuggyModel(String[] tripsFromNCBS, String[] tripsFromMandara, String from, String to) {
        this.tripsFromNCBS = tripsFromNCBS;
        this.tripsFromMandara = tripsFromMandara;
        this.nextTrip = nextTrip();
        this.from = from;
        this.to = to;
        if(from.equals("ncbs")){
            this.routeNo = TransportConstants.ROUTE_BUGGY_NCBS;
        }
        else {this.routeNo = TransportConstants.ROUTE_BUGGY_MANDARA;}
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String[] getTripsFromNCBS() {
        return tripsFromNCBS;
    }

    public void setTripsFromNCBS(String[] tripsFromNCBS) {
        this.tripsFromNCBS = tripsFromNCBS;
    }

    public String[] getTripsFromMandara() {
        return tripsFromMandara;
    }

    public void setTripsFromMandara(String[] tripsFromMandara) {
        this.tripsFromMandara = tripsFromMandara;
    }

    public String[] getNextTrip() {
        return nextTrip;
    }

    private String[] nextTrip() {
        int ncbs_trip = new TransportHelper().getTripNumber(Arrays.asList(tripsFromNCBS));
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE,1);
        int tomorrow = cal.get(Calendar.DAY_OF_WEEK);
        int ncbsDay = today;
        int mandaraDay = today;
        if (ncbs_trip == TransportHelper.DEFAULT_NO) {
            ncbs_trip = 0;
            ncbsDay = tomorrow;
        }
        int mandara_trip = new TransportHelper().getTripNumber(Arrays.asList(tripsFromMandara));
        if (mandara_trip == TransportHelper.DEFAULT_NO) {
            mandara_trip = 0;
            mandaraDay = tomorrow;
        }
        return new String[]{tripsFromNCBS[ncbs_trip], tripsFromMandara[mandara_trip], String.valueOf(ncbsDay), String.valueOf(mandaraDay)};
    }

    public int getRouteNo() {
        return routeNo;
    }
}
