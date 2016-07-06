package com.rohitsuratekar.NCBSinfo.constants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public interface RemoteData {

    interface nodes {

        //Path data
        String USER_NODE = "users";
        String PUBLIC_NODE = "public";
    }

    interface data {
        //User Specific data
        String USERNAME = "username";
        String EMAIL = "email";
        String TOKEN = "token";
        String DEFAULT_ROUTE = "defaultRoute";
        String LATEST_APP = "latestApp";
    }

}