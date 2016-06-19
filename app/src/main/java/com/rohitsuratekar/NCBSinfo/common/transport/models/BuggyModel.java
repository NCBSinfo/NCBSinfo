package com.rohitsuratekar.NCBSinfo.common.transport.models;

import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;

import java.util.Arrays;

public class BuggyModel {

    String[] tripsFromNCBS;
    String[] tripsFromMandara;
    String[] nextTrip;

    public BuggyModel(String[] tripsFromNCBS, String[] tripsFromMandara) {
        this.tripsFromNCBS = tripsFromNCBS;
        this.tripsFromMandara = tripsFromMandara;
        this.nextTrip = nextTrip();
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
        if (ncbs_trip == TransportHelper.DEFAULT_NO) {
            ncbs_trip = 0;
        }
        int mandara_trip = new TransportHelper().getTripNumber(Arrays.asList(tripsFromMandara));
        if (mandara_trip == TransportHelper.DEFAULT_NO) {
            mandara_trip = 0;
        }
        return new String[]{tripsFromNCBS[ncbs_trip], tripsFromMandara[mandara_trip]};
    }

}
