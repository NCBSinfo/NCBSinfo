package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalDetails {
    @SerializedName("currentApp")
    @Expose
    private Integer currentApp;
    @SerializedName("defaultRoute")
    @Expose
    private Integer defaultRoute;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firebaseID")
    @Expose
    private String firebaseID;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("lastSync")
    @Expose
    private String lastSync;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notificationPreference")
    @Expose
    private Integer notificationPreference;
    @SerializedName("requestCalls")
    @Expose
    private String requestCalls;
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getCurrentApp() {
        return currentApp;
    }

    public void setCurrentApp(Integer currentApp) {
        this.currentApp = currentApp;
    }

    public Integer getDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(Integer defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNotificationPreference() {
        return notificationPreference;
    }

    public void setNotificationPreference(Integer notificationPreference) {
        this.notificationPreference = notificationPreference;
    }

    public String getRequestCalls() {
        return requestCalls;
    }

    public void setRequestCalls(String requestCalls) {
        this.requestCalls = requestCalls;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
