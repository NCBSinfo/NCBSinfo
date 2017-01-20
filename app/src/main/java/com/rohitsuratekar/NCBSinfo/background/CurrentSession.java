package com.rohitsuratekar.NCBSinfo.background;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;

import java.util.List;

/**
 * This static object hold information about all routes and current state of app.
 * This will reduce computation on retrieving route data again and again
 */

public class CurrentSession {

    private List<Route> allRoutes;
    private Route currentRoute;
    private int currentIndex;
    private List<String> nextList;

    public List<Route> getAllRoutes() {
        return allRoutes;
    }

    public void setAllRoutes(List<Route> allRoutes) {
        this.allRoutes = allRoutes;
    }

    public void setRouteInfo(List<Route> allRoutes, Route currentRoute, int currentIndex) {
        this.allRoutes = allRoutes;
        this.currentRoute = currentRoute;
        this.currentIndex = currentIndex;
    }

    public List<String> getNextList() {
        return nextList;
    }

    public void setNextList(List<String> nextList) {
        this.nextList = nextList;
    }

    public static CurrentSession getSession() {
        return session;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    private static final CurrentSession session = new CurrentSession();

    public static CurrentSession getInstance() {
        return session;
    }
}
