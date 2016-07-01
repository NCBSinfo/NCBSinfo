package com.rohitsuratekar.NCBSinfo.interfaces;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public interface AlarmConstants {


    String DAILY_FETCH = "alarm_dailyFetch";
    String RESET_ALL = "alarm_resetAll";
    String SEND_UPCOMINGS = "alarm_sendUpcomings";
    String SEND_NOTIFICATION = "alarm_sendNotifications";
    String NOTIFICATION_CODE = "alarm_notificationCode";

    interface dailyAlarms {

        interface earlyMorning {
            int ID = 1989;
            int TIME = 6;
        }

        interface morning {
            int ID = 1990;
            int TIME = 10;
        }

        interface afternoon {
            int ID = 1991;
            int TIME = 12;
        }

        interface evening {
            int ID = 1992;
            int TIME = 18;
        }

    }


}
