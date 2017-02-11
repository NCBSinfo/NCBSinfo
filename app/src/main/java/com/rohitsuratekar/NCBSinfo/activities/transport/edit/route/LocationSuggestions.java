package com.rohitsuratekar.NCBSinfo.activities.transport.edit.route;

class LocationSuggestions {

    private String origin;
    private String destination;
    private String type;

    public LocationSuggestions(String origin, String destination, String type) {
        this.origin = origin;
        this.destination = destination;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
