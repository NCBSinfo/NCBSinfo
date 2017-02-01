package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CounterDetails {
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("user")
    @Expose
    private String user;

    public CounterDetails(String route, int count) {
        this.route = route;
        this.count = count;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
