package com.rohitsuratekar.NCBSinfo.activities.transport.edit.route;

class LocationSuggestions {

    private String origin;
    private String destination;

    public LocationSuggestions(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
