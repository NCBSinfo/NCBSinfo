package com.rohitsuratekar.NCBSinfo.activities.background.events;

public class SplashLoadingEvent {

    private String event;

    public SplashLoadingEvent(String event) {
        this.event = event;
    }

    public String checkEvent() {
        return event;
    }
}
