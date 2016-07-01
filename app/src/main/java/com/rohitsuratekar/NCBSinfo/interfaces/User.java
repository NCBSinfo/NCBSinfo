package com.rohitsuratekar.NCBSinfo.interfaces;

public interface User {

    String MODE = "app_mode";
    String ONLINE = "online";
    String OFFLINE = "offline";

    interface profile {
        String REGISTERED = "registeredUser";  //Boolen
        String NAME = "currentUsername"; //String
        String EMAIL = "currentEmail"; //String
        String RESEARCH_TALK = "currentResearchTalk"; //Int
        String FIREBASE_TOKEN = "firebaseRegistrationToken"; //String
    }

    /**
     * All newly registered will be "New Users" first, Once then sent registration details, they will become "Regular Users"
     * All users logged in successfully will be "Old users" first, Once then sent registration details, they will become "Regular Users"
     */


    String USER_TYPE = "userType";

    interface currentUserType {
        String NEW_USER = "newUser";
        String OLD_USER = "oldUser";
        String REGULAR_USER = "regularUser";
    }

    interface preferences {

        String NOTIFICATIONS = "notification_preference"; //boolean
        String NOTIFICANTION_ONSET = "pref_notificationOnset"; //int
        String DEFAULT_ROUTE = "defaultRoute"; //int

    }

    interface netwrok {
        String REGISTRATION_DETAILS_SENT = "registrationDetails";
        String LAST_REFRESH_REMOTE_CONFIG = "lastRefreshRemoteConfig";
        String LAST_DATA_FETCH = "lastDataFetch";
        String IS_OLD_VERSION = "isOldVersion";
    }

    interface firstTime {
        String APP_OPEN = "firstTimeAppOpen";
        String DATA_MIGRATED = "dataMIgrated";
        String CAMP_NOTICE = "camp_notice_firstTime";
        String CAMP_EVENTS_FETCHED = "campEventFetched";
        String FIRST_NOTIFICATION_EVENTS = "sendFirstNotificationEvents";
        String FIRST_NOTIFICATION_DASHBOARD = "sendFirstNotificationDashboard";
        String CANCELED_PAST_ALARMS = "cancelledPastAlarms";
    }
}
