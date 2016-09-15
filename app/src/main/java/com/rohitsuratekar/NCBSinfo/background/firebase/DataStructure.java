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
    @SerializedName("lastSync")
    @Expose
    private String lastSync;
    @SerializedName("requestCalls")
    @Expose
    private String requestCalls;
    @SerializedName("notificationPreference")
    @Expose
    private int notificationPreference; //Use int as follows : 0 : default, 1:true, 2:false
    @SerializedName("webLogin")
    @Expose
    private String webLogin;
    @SerializedName("webSync")
    @Expose
    private String webSync;
    @SerializedName("webPref")
    @Expose
    private String webPref;

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

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getWebLogin() {
        return webLogin;
    }

    public void setWebLogin(String webLogin) {
        this.webLogin = webLogin;
    }

    public String getWebSync() {
        return webSync;
    }

    public void setWebSync(String webSync) {
        this.webSync = webSync;
    }

    public String getWebPref() {
        return webPref;
    }

    public void setWebPref(String webPref) {
        this.webPref = webPref;
    }
}