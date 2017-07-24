package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;

/**
 * Created by Rohit Suratekar on 22-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class AppPrefs extends Preferences {

    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";

    public AppPrefs(Context context) {
        super(context);
    }

    public String getFavoriteOrigin() {
        return get(FAVORITE_ORIGIN, "ncbs");
    }

    public String getFavoriteDestination() {
        return get(FAVORITE_DESTINATION, "iisc");
    }

    public String getFavoriteType() {
        return get(FAVORITE_TYPE, "other");
    }

    public void setFavoriteOrigin(String origin) {
        put(FAVORITE_ORIGIN, origin);
    }

    public void setFavoriteDestination(String destination) {
        put(FAVORITE_DESTINATION, destination);
    }

    public void setFavoriteType(String type) {
        put(FAVORITE_TYPE, type);
    }

    public void setUserName(String username) {
        put(USER_NAME, username);
    }

    public String getUserName() {
        return get(USER_NAME, "User");
    }

    public String getUserEmail() {
        return get(USER_EMAIL, "email@domain.com");
    }

    public void setUserEmail(String email) {
        put(USER_EMAIL, email);
    }

}
