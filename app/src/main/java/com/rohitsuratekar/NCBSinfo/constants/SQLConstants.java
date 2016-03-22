package com.rohitsuratekar.NCBSinfo.constants;

/**
 * Created by Dexter on 3/16/2016.
 */
public class SQLConstants {

    public static final int DATABASE_VERSION = 1;  //Update this every time you change database
    public static final String DATABASE_NAME = "NCBSinfo";


    //Contact Table

    public static final String CONTACT_TABLENAME = "contacts";
    public static final String CONTACT_KEY_ID = "contact_id";
    public static final String CONTACT_KEY_NAME = "contact_name";
    public static final String CONTACT_KEY_DEPARTMENT = "contact_department";
    public static final String CONTACT_KEY_POSITION = "contact_position";
    public static final String CONTACT_KEY_EXTENSION = "contact_extension";
    public static final String CONTACT_KEY_FAVORITE = "contact_favorite";


    //Notification Table
    public static final String NOTE_TABLENAME = "user_log";
    public static final String NOTE_KEY_ID = "note_id";
    public static final String NOTE_TIMESTAMP = "note_timestamp";
    public static final String NOTE_TITLE = "note_notificationTitle";
    public static final String NOTE_MESSAGE = "note_notificationMessage";
    public static final String NOTE_TOPIC = "note_topic";
    public static final String NOTE_SHOW_MESSAGE = "note_showMessage";
}
