package com.rohitsuratekar.NCBSinfo.activities.transport.models;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 * <p>
 * This method is simple collection of properties needed for setting up UI
 */

class TripReport {

    private String rawTrip;
    private boolean isFromSameList;
    private int index;

    TripReport(String rawTrip, boolean isFromSameList) {
        this.rawTrip = rawTrip;
        this.isFromSameList = isFromSameList;
    }

    TripReport(String rawTrip, boolean isFromSameList, int index) {
        this.rawTrip = rawTrip;
        this.isFromSameList = isFromSameList;
        this.index = index;
    }

    String getRawTrip() {
        return rawTrip;
    }

    void setRawTrip(String rawTrip) {
        this.rawTrip = rawTrip;
    }

    boolean isFromSameList() {
        return isFromSameList;
    }

    void setFromSameList(boolean fromSameList) {
        isFromSameList = fromSameList;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }
}
