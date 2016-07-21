package com.rohitsuratekar.NCBSinfo.background.remote;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public interface RemoteData {


    interface status {
        String NORMAL = "normal";
        String DEFAULT = "default";
        String URGENT = "urgent";
    }


    interface triggers {

        String CHECK = "check";
        String UPDATE = "update";
    }


    interface levels {

        String APP = "app"; //App level
        String TRANSPORT = "transport"; //Transport level
        String SPECIAL = "special"; //Special level
    }


    //All Keys
    interface keys {
        String CURRENT_APP = "version"; //Latest Version of App
        String TIMESTAMP = "timestamp"; //Last Updated Data
        String MESSAGE = "message"; //Special Message
    }
}
