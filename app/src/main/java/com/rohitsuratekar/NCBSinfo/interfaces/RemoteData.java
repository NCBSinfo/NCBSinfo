package com.rohitsuratekar.NCBSinfo.interfaces;

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
        String CAMP_NODE = "camp2016";

    }

    interface data {
        //User Specific data
        String USERNAME = "username";
        String EMAIL = "email";
        String TOKEN = "token";
        String RESEARCH_TALK = "ResearchTalk";
        String DEFAULT_ROUTE = "defaultRoute";
    }


    //Cache expiration
    long CACHE_EXPIRATION = 3600 * 12; //Seconds
}
