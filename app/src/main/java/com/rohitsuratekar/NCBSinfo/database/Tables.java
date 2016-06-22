package com.rohitsuratekar.NCBSinfo.database;

import android.database.sqlite.SQLiteDatabase;

public class Tables {

    SQLiteDatabase db;

    public Tables(SQLiteDatabase db) {
        this.db = db;
    }

    public void makeContactTable() {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactsData.TABLE_CONTACTS + "("
                + ContactsData.CONTACT_KEY_ID + " INTEGER PRIMARY KEY,"
                + ContactsData.CONTACT_KEY_NAME + " TEXT,"
                + ContactsData.CONTACT_KEY_DEPARTMENT + " TEXT,"
                + ContactsData.CONTACT_KEY_POSITION + " TEXT,"
                + ContactsData.CONTACT_KEY_EXTENSION + " TEXT,"
                + ContactsData.CONTACT_KEY_FAVORITE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    public void makeTalkTable() {
        String CREATE_TALK_TABLE = "CREATE TABLE " + TalkData.TABLE_TALK + "("
                + TalkData.TALK_KEY_ID + " INTEGER PRIMARY KEY,"
                + TalkData.TALK_TIMESTAMP + " TEXT,"
                + TalkData.TALK_NOTIFICATION_TITLE + " TEXT,"
                + TalkData.TALK_DATE + " TEXT,"
                + TalkData.TALK_TIME + " TEXT,"
                + TalkData.TALK_VENUE + " TEXT,"
                + TalkData.TALK_SPEAKER + " TEXT,"
                + TalkData.TALK_AFFILICATION + " TEXT,"
                + TalkData.TALK_TITLE + " TEXT,"
                + TalkData.TALK_HOST + " TEXT,"
                + TalkData.TALK_DATACODE + " TEXT,"
                + TalkData.TALK_ACTIONCODE + " INTEGER,"
                + TalkData.TALK_DATA_ACTION + " TEXT )";
        db.execSQL(CREATE_TALK_TABLE);
    }

    public void makeConferenceTable() {
        String CREATE_CONFERENCE_TABLE = "CREATE TABLE " + ConferenceData.TABLE_CONFERENCE + "("
                + ConferenceData.CONFERENCE_KEY_ID + " INTEGER PRIMARY KEY,"
                + ConferenceData.CONFERENCE_TIMESTAMP + " TEXT,"
                + ConferenceData.CONFERENCE_CODE + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_ID + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_TITLE + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_SPEAKER + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_HOST + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_START_TIME + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_END_TIME + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_DATE + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_VENUE + " TEXT,"
                + ConferenceData.CONFERENCE_EVENT_MESSAGE + " TEXT, "
                + ConferenceData.CONFERENCE_EVENT_CODE + " TEXT, "
                + ConferenceData.CONFERENCE_UPDATE_COUNTER + " INTEGER " + ")";
        db.execSQL(CREATE_CONFERENCE_TABLE);
    }

    public void makeNotificationTable() {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + NotificationData.TABLE_NOTIFICATIONS + "("
                + NotificationData.KEY_ID + " INTEGER PRIMARY KEY,"
                + NotificationData.TIMESTAMP + " TEXT,"
                + NotificationData.TITLE + " TEXT,"
                + NotificationData.MESSAGE + " TEXT,"
                + NotificationData.FROM + " TEXT,"
                + NotificationData.EXTRA_VARIABLES + " TEXT " + ")";
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }


    public void dropAllTables() {
        db.execSQL("DROP TABLE IF EXISTS " + ContactsData.TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TalkData.TABLE_TALK);
        db.execSQL("DROP TABLE IF EXISTS " + ConferenceData.TABLE_CONFERENCE);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationData.TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TalkData.TABLE_OLD_TALK);
    }

}
