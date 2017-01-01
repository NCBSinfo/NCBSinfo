package com.rohitsuratekar.NCBSinfo.activities.transport;

public class TransportLocation {
    private String origin;
    private String destination;
    private int icon;
    private boolean favorite;
    private TransportType type;

    public TransportLocation() {
    }


    public TransportLocation(String origin, String destination, int icon, TransportType type, boolean favorite) {
        this.type = type;
        this.origin = origin;
        this.destination = destination;
        this.icon = icon;
        this.favorite = favorite;
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
