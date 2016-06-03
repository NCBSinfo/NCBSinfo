package com.rohitsuratekar.NCBSinfo.constants;

public class SQL {

    //Global Names
    public static final int DATABASE_VERSION = 6; //Changed from 4 to 5 on 2 June 2016 //TODO change to 5
    public static final String DATABASE_NAME = "NCBSinfo";

    //Tables
    public static final String TABLE_LOG = "table_log";
    public static final String TABLE_DATABASE = "table_database";
    public static final String TABLE_TALK = "table_talkdata";
    public static final String TABLE_CONTACTS = "table_contacts";
    public static final String TABLE_EXTERNAL = "table_external";
    public static final String TABLE_CONFERENCE = "table_conference";

    //Log Table Columns
    public static final String LOG_ID = "log_id";
    public static final String LOG_TIMESTAMP = "log_timestamp";
    public static final String LOG_MESSAGE = "log_message";
    public static final String LOG_DETAILS = "log_details";
    public static final String LOG_CATEGORY = "log_category";
    public static final String LOG_STATUSCODE = "log_status_code";
    public static final String LOG_STATUS = "log_status";

    //Form Database Table
    public static final String DATA_KEY_ID = "data_id";
    public static final String DATA_TIMESTAMP = "data_timestamp";
    public static final String DATA_TITLE = "data_notificationTitle";
    public static final String DATA_DATE = "data_date";
    public static final String DATA_TIME = "data_time";
    public static final String DATA_VENUE = "data_venue";
    public static final String DATA_SPEAKER = "data_speaker";
    public static final String DATA_ABSTRACT = "data_talkabstract";
    public static final String DATA_URL = "data_url";
    public static final String DATA_NEXTSPEAKER = "data_nextspeaker";
    public static final String DATA_DATACODE = "data_datacode";
    public static final String DATA_ACTIONCODE = "data_actioncode";

    //talk Database Table
    public static final String TALK_KEY_ID = "talk_id";
    public static final String TALK_TIMESTAMP = "talk_timestamp";
    public static final String TALK_NOTIFICATION_TITLE = "talk_notificationTitle";
    public static final String TALK_DATE = "talk_date";
    public static final String TALK_TIME = "talk_time";
    public static final String TALK_VENUE = "talk_venue";
    public static final String TALK_SPEAKER = "talk_speaker";
    public static final String TALK_AFFILICATION = "talk_talkabstract";
    public static final String TALK_TITLE = "talk_url";
    public static final String TALK_HOST = "talk_nextspeaker";
    public static final String TALK_DATACODE = "talk_talkcode";
    public static final String TALK_ACTIONCODE = "talk_actioncode";

    //Database Action codes
    public static final int ACTION_RETRIVED = 1;
    public static final int ACTION_NOTIFIED = 2;
    public static final int ACTION_SEND = 3;

    //Contact Table
    public static final String CONTACT_KEY_ID = "contact_id";
    public static final String CONTACT_KEY_NAME = "contact_name";
    public static final String CONTACT_KEY_DEPARTMENT = "contact_department";
    public static final String CONTACT_KEY_POSITION = "contact_position";
    public static final String CONTACT_KEY_EXTENSION = "contact_extension";
    public static final String CONTACT_KEY_FAVORITE = "contact_favorite";

    //External Table
    public static final String EXTERNAL_KEY_ID = "external_id";
    public static final String EXTERNAL_TIMESTAMP = "external_timestamp";
    public static final String EXTERNAL_CODE = "external_code";
    public static final String EXTERNAL_TITLE = "external_title";
    public static final String EXTERNAL_MESSAGE = "external_message";
    public static final String EXTERNAL_EXTRA = "external_extra";


    //Conference Table
    public static final String CONFERENCE_KEY_ID = "conference_id";
    public static final String CONFERENCE_TIMESTAMP = "conference_timestamp";
    public static final String CONFERENCE_CODE = "conference_code";
    public static final String CONFERENCE_EVENT_TITLE = "conference_event_title";
    public static final String CONFERENCE_EVENT_SPEAKER = "conference_event_speaker";
    public static final String CONFERENCE_EVENT_HOST = "conference_event_host";
    public static final String CONFERENCE_EVENT_START_TIME = "conference_event_startTime";
    public static final String CONFERENCE_EVENT_END_TIME = "conference_event_endTime";
    public static final String CONFERENCE_EVENT_DATE = "conference_event_date";
    public static final String CONFERENCE_EVENT_VENUE = "conference_event_venue";
    public static final String CONFERENCE_EVENT_MESSAGE = "conference_event_message";
    public static final String CONFERENCE_EVENT_CODE = "conference_event_code";
    public static final String CONFERENCE_UPDATE_COUNTER = "conference_update_counter";

}
