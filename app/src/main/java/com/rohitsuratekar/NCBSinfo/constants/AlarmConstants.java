package com.rohitsuratekar.NCBSinfo.constants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public interface AlarmConstants {


    enum alarmLevel {

        NETWORK,
        TRANSPORT
    }

    enum alarmType {

        REPEAT,
        SINGLE_SHOT
    }

    enum alarmTriggers {

        DAILY_FETCH, //Daily network fetch
        LOW_PRIORITY_ALARMS, //Low priority alarms for remote data fetch
        RESET_ALL, //Deletes all alarms and sets daily
        SEND_UPCOMING, //Sends upcoming event notification alarms
        SEND_NOTIFICATION, //Sends notification
        TRANSPORT_REMINDER, //Trigger to send transport reminder
        DELETE_ALARM, //deletes all
        DELETE_REMINDERS, //delete reminders
        SET_ALARM //Sets alarms
    }


}
