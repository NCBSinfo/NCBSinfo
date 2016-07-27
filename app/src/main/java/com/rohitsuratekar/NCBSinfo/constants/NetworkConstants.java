package com.rohitsuratekar.NCBSinfo.constants;

/**
 * Important: Keep this interface un-tracked from your version control.
 * This information should not be available easily to public
 * Anyone can decompile app and get this anyways :P but just make harder to get :D
 */
public interface NetworkConstants {

    //All FCM topics will go here
    interface fcmTopics {
        String PUBLIC = "public";
        String EMERGENCY = "emergency";
        String STUDENT = "students";
        String CAMP16 = "camp16"; //Remove after topic is deleted
        String DEBUG = "debug";
    }

    interface fcmKeys {
        String TITLE = "title";
        String MESSAGE = "message";
        String RCODE = "rcode";
        String EXTRA = "extra";

        interface values {
            String EXTRA_PERSONAL = "personal";
        }
    }

    //All fusion tables
    interface tables {
        String RESEARCH_TALK = "1ZpvvQjpUK5pfls4yENudDtKYRxXWWTg9Zr_PtqFN";
        String REMOTE_TABLE = "1NzS2kS0w1_kYMAHGR1iAtQ-uNUi-OqsexXhP-c5-";
    }

    //All triggers
    interface fcmTriggers {
        String NEW_UPDATE = "NewUpdate";
        String DATA_SYNC = "DataSync";
        String DEBUG = "debug";
    }

    //Google Form Links
    interface form {

        interface registration {
            String REGISTRATION_URL = "forms/d/1wdTuzR0R_H7MBbgaraqH9rI68zTjE1EL6hlY29dCEwc/formResponse";
            String NAME = "entry_675707263";
            String EMAIL = "entry_1261125575";
            String TOKEN = "entry_1147357417";
            String FIREBASE_ID = "entry_1161849798";
            String EXTERNAL_CODE = "entry_1750062233";
        }
    }

    String API_KEY = "AIzaSyB9uRal1LYEo14APa8eJAwQzTtMCFdI9Io";

}
