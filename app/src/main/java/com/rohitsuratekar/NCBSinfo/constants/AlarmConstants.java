package com.rohitsuratekar.NCBSinfo.constants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public interface AlarmConstants {


    enum alarmLevel {

        NETWORK,
        NOTIFICATION,
        TRANSPORT
    }

    enum alarmType {

        REPEAT,
        SINGLE_SHOT
    }

    enum alarmTriggers {

        DAILY_FETCH,
        RESET_ALL,
        SEND_UPCOMING,
        SEND_NOTIFICATION,
        TRANSPORT_REMINDER,
        DELETE_ALARM
    }


}
