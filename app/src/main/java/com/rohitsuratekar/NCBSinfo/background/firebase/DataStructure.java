package com.rohitsuratekar.NCBSinfo.background.firebase;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 04-07-16.
 */
@IgnoreExtraProperties
public class DataStructure implements FireBaseConstants {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("defaultRoute")
    @Expose
    private int defaultRoute;
    @SerializedName("firebaseID")
    @Expose
    private String firebaseID;
    @SerializedName("currentApp")
    @Expose
    private int currentApp;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("requestCalls")
    @Expose
    private String requestCalls;
    @SerializedName("notificationPreference")
    @Expose
    private int notificationPreference; //Use int as follows : 0 : default, 1:true, 2:false

    public int isNotificationPreference() {
        return notificationPreference;
    }

    public void setNotificationPreference(int notificationPreference) {
        this.notificationPreference = notificationPreference;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRequestCalls() {
        return requestCalls;
    }

    public void setRequestCalls(String requestCalls) {
        this.requestCalls = requestCalls;
    }

    public int getCurrentApp() {
        return currentApp;
    }

    public void setCurrentApp(int currentApp) {
        this.currentApp = currentApp;
    }

    public int getDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(int defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}