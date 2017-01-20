package com.rohitsuratekar.NCBSinfo.activities.transport.models;

public enum TransportType {
    SHUTTLE(30),
    BUGGY(12),
    TTC(20);

    private int seats;

    TransportType(int seats) {
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

}
