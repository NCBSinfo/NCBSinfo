package com.rohitsuratekar.NCBSinfo.constants;

public class StatusCodes {

    //App based status
    public static final int STATUS_OPENED = 1;
    public static final int STATUS_LOGINSKIP = 2;
    public static final int STATUS_REGISTRATION_STARTED = 15;
    public static final int STATUS_USER_REGISTERED = 16;
    public static final int STATUS_TOPIC_SUBSCRIBED = 17;
    public static final int STATUS_TOPIC_UNSUBSCRIBED = 18;
    public static final int STATUS_SENDFORM_FAIL = 19;
    public static final int STATUS_REGISTRATION_ERROR = 20;

    //Daily networking
    public static final int STATUS_SERVICESTART = 3;
    public static final int STATUS_DATARETRIVED = 4;

    //Alarms
    public static final int STATUS_ALARMCALL = 5;
    public static final int STATUS_ALARMCHANGED = 6;
    public static final int STATUS_AFTERBOOT = 7;
    public static final int STATUS_ONUPGRADE = 8;
    public static final int STATUS_ALARM_FAILED = 14;

    //Notifications
    public static final int STATUS_DAILYNOTIFICATIONS = 9;
    public static final int STATUS_FAILED_NOTIFICATIONS = 10;
    public static final int STATUS_EVENTNOTIFIED = 11;
    public static final int STATUS_SET_NOTIFICATION = 12;
    public static final int STATUS_DAILYNOTE_RESET = 13;

    //GCM
    public static final int STATUS_GCM_RECEIVED = 21;
    public static final int STATUS_PUBLIC_GCM = 22;
    public static final int STATUS_NO_TOPIC = 23;
    public static final int STATUS_ERROR_DELETING = 24;


    //Standard status
    public static final int STATUS_SUCCESSFUL = 200;
    public static final int STATUS_UNAUTHENTICATED = 401;
    public static final int STATUS_FORBIDDEN = 403 ;


    //Status
    public static final int TYPE_URGENT= 1000;
    public static final int TYPE_SUCCESSFUL = 2000;
    public static final int TYPE_ERROR = 3000;
    public static final int TYPE_FAILED = 4000;
    public static final int TYPE_UNKNOWN = 5000;


    //Category
    public static final String STATCAT_EMERGENCY = "emergency";
    public static final String STATCAT_DAILY = "daily";
    public static final String STATCAT_NETWORK = "network";
    public static final String STATCAT_APPLICATION = "application";
    public static final String STATCAT_IMPORTANT = "important";
    public static final String STATCAT_BACKGROUND = "background";

}
