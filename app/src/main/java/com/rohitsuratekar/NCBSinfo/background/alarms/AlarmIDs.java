package com.rohitsuratekar.NCBSinfo.background.alarms;

/**
 * Alarm IDs for all alarms set by this application
 */
public interface AlarmIDs {

    //Daily default alarms

    enum daily {

        DAILY_EARLY_MORNING(1989, 6), //Since Version 21
        DAILY_MORNING (1990, 10), //Since Version 21
        DAILY_AFTERNOON (1991, 12), //Since Version 21
        DAILY_EVENING  (1992, 18); //Since Version 21

        private final int alarmID;
        private final int hourOfTheDay;

        /**
         *
         * @param alarmID : Alarm ID
         * @param hourOftheDay : When alarm should be received
         */
        daily(int alarmID, int hourOftheDay) {
            this.alarmID = alarmID;
            this.hourOfTheDay = hourOftheDay;
        }
        public int getAlarmID() {
            return alarmID;
        }
        public int getHourOftheDay() {
            return hourOfTheDay;
        }

    }


    /**
     * Deprecated IDs
     *
     * Always check if these ID's exists or not and cancel them on the first app run.
     * Remove this section after significant users are shifted to new version.
     * DO not use multiple IDs with same integer
     */
    enum  old {
        OLD1(2000),
        OLD2(2003),
        OLD3(2004),
        OLD4(2005),
        OLD5(2006);


        private final int idNumber;
        old(int idNumber) {
            this.idNumber = idNumber;
        }
        public int getIdNumber() {
            return idNumber;
        }
    }
}
