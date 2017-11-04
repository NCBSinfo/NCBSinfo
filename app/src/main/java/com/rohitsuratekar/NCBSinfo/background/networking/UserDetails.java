package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */


public class UserDetails {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("FcmToken")
    @Expose
    private String Fcmtoken;
    @SerializedName("favoriteOrigin")
    @Expose
    private String favoriteOrigin;
    @SerializedName("favoriteDestination")
    @Expose
    private String favoriteDestination;
    @SerializedName("favoriteType")
    @Expose
    private String favoriteType;
    @SerializedName("accountCreationDate")
    @Expose
    private String accountCreationDate;
    @SerializedName("lastLogin")
    @Expose
    private String lastLogin;
    @SerializedName("lastSync")
    @Expose
    private String lastSync;
    @SerializedName("notifications")
    @Expose
    private String notifications;

    @SerializedName("migrationID")
    @Expose
    private String migrationID;


    @SerializedName("routes")
    @Expose
    private Map<String, Object> routes;

    public UserDetails() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcmToken() {
        return Fcmtoken;
    }

    public void setFcmToken(String token) {
        this.Fcmtoken = token;
    }

    public String getFavoriteOrigin() {
        return favoriteOrigin;
    }

    public void setFavoriteOrigin(String favoriteOrigin) {
        this.favoriteOrigin = favoriteOrigin;
    }

    public String getFavoriteDestination() {
        return favoriteDestination;
    }

    public void setFavoriteDestination(String favoriteDestination) {
        this.favoriteDestination = favoriteDestination;
    }

    public String getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(String favoriteType) {
        this.favoriteType = favoriteType;
    }

    public String getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(String accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
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

    public String getNotifications() {
        return notifications;
    }

    public Map<String, Object> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Object> routes) {
        this.routes = routes;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getMigrationID() {
        return migrationID;
    }

    public void setMigrationID(String migrationID) {
        this.migrationID = migrationID;
    }
}