package com.rohitsuratekar.NCBSinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rohitsuratekar.NCBSinfo.constants.SQLConstants;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_database;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = SQLConstants.DATABASE_VERSION;

    // Database Name
    private static final String DATABASE_NAME = SQLConstants.DATABASE_NAME;


    // Table Lists
    private static final String TABLE_CONTACT = SQLConstants.CONTACT_TABLENAME;
    private static final String TABLE_NOTE = SQLConstants.NOTE_TABLENAME;



    // Contacts Table Columns names
    private static final String CONTACT_KEY_ID = SQLConstants.CONTACT_KEY_ID;
    private static final String CONTACT_KEY_NAME = SQLConstants.CONTACT_KEY_NAME;
    private static final String CONTACT_KEY_DEPARTMENT = SQLConstants.CONTACT_KEY_DEPARTMENT;
    private static final String CONTACT_KEY_POSITION = SQLConstants.CONTACT_KEY_POSITION;
    private static final String CONTACT_KEY_EXTENSION = SQLConstants.CONTACT_KEY_EXTENSION;
    private static final String CONTACT_KEY_FAVORITE = SQLConstants.CONTACT_KEY_FAVORITE;



    // Notification User Log Table Columns names
    private static final String NOTE_KEY_ID = SQLConstants.NOTE_KEY_ID;
    private static final String NOTE_TIMESTAMP = SQLConstants.NOTE_TIMESTAMP;
    private static final String NOTE_TITLE = SQLConstants.NOTE_TITLE;
    private static final String NOTE_MESSAGE = SQLConstants.NOTE_MESSAGE;
    private static final String NOTE_TOPIC = SQLConstants.NOTE_TOPIC;
    private static final String NOTE_SHOW_MESSAGE = SQLConstants.NOTE_SHOW_MESSAGE;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + CONTACT_KEY_ID + " INTEGER PRIMARY KEY," + CONTACT_KEY_NAME + " TEXT,"
                + CONTACT_KEY_DEPARTMENT + " TEXT," + CONTACT_KEY_POSITION + " TEXT," + CONTACT_KEY_EXTENSION + " TEXT," +  CONTACT_KEY_FAVORITE + " TEXT " + ")";

        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + NOTE_KEY_ID + " INTEGER PRIMARY KEY," + NOTE_TIMESTAMP + " TEXT,"
                + NOTE_TITLE + " TEXT," + NOTE_MESSAGE + " TEXT," + NOTE_TOPIC + " TEXT," + NOTE_SHOW_MESSAGE + " TEXT"+")";

        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        // Create tables again
        onCreate(db);
    }


    /**
     * Operations related to contacts
     */
    //Operations
    // Adding new contact
    public void addContact(models_contacts_database contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_NAME, contact.getContact_name()); // Contact Name
        values.put(CONTACT_KEY_DEPARTMENT, contact.getContact_department()); // Contact Department
        values.put(CONTACT_KEY_POSITION, contact.getContact_position()); // Contact Position
        values.put(CONTACT_KEY_EXTENSION, contact.getContact_extension()); // Contact extension
        values.put(CONTACT_KEY_FAVORITE, contact.getContact_favorite()); // Contact favorite

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);  //Null is to autoincriment
        db.close(); // Closing database connection
    }

    // Getting single Document
    public models_contacts_database getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACT, new String[] { CONTACT_KEY_ID, CONTACT_KEY_NAME, CONTACT_KEY_DEPARTMENT, CONTACT_KEY_POSITION, CONTACT_KEY_EXTENSION, CONTACT_KEY_FAVORITE }, CONTACT_KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();

        }
        models_contacts_database contact = new models_contacts_database(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        cursor.close();
        return contact;
    }

    // Getting All Contacts
    public List<models_contacts_database> getAllContacts() {
        List<models_contacts_database> contactList = new ArrayList<models_contacts_database>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                models_contacts_database contact = new models_contacts_database();
                contact.setContact_id(Integer.parseInt(cursor.getString(0)));
                contact.setContact_name(cursor.getString(1));
                contact.setContact_department(cursor.getString(2));
                contact.setContact_position(cursor.getString(3));
                contact.setContact_extension(cursor.getString(4));
                contact.setContact_favorite(cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return contactList;
    }

    // Updating single contact
    public int updateContact(models_contacts_database contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_NAME, contact.getContact_name()); // Contact Name
        values.put(CONTACT_KEY_DEPARTMENT, contact.getContact_department()); // Contact Department
        values.put(CONTACT_KEY_POSITION, contact.getContact_position()); // Contact Position
        values.put(CONTACT_KEY_EXTENSION, contact.getContact_extension()); // Contact extension
        values.put(CONTACT_KEY_FAVORITE, contact.getContact_favorite()); // Contact favorite

        // updating row
        return db.update(TABLE_CONTACT, values, CONTACT_KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getContact_id()) });
    }

    // Deleting single contact
    public void deleteContact(models_contacts_database contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getContact_id()) });
        db.close();

    }


    // Getting contact count
    public int getContactCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    /**
     * Operations related to User notification Log
     */

    // Adding new entry
    public void addNotificationEntry(models_userNotifications enrty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_TIMESTAMP, enrty.getTimestamp());
        values.put(NOTE_TITLE, enrty.getNotificationTitle());
        values.put(NOTE_MESSAGE, enrty.getNotificationMessage());
        values.put(NOTE_TOPIC, enrty.getNotificationTopic());
        values.put(NOTE_SHOW_MESSAGE, enrty.getShowmessage());
        // Inserting Row
        db.insert(TABLE_NOTE, null, values);  //Null is to autoincriment
        db.close(); // Closing database connection
    }

    // Getting single entry
    models_userNotifications getNotificationEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, new String[]{NOTE_KEY_ID, NOTE_TIMESTAMP, NOTE_TITLE, NOTE_MESSAGE, NOTE_TOPIC, NOTE_SHOW_MESSAGE}, NOTE_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        models_userNotifications entry = new models_userNotifications(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
        // return contact
        cursor.close();
        db.close();
        return entry;
    }

    // Getting All Contacts
    public List<models_userNotifications> getAllEntries() {
        List<models_userNotifications> entryList = new ArrayList<models_userNotifications>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                models_userNotifications model = new models_userNotifications();
                model.setMessageID(Integer.parseInt(cursor.getString(0)));
                model.setTimestamp(cursor.getString(1));
                model.setNotificationTitle(cursor.getString(2));
                model.setNotificationMessage(cursor.getString(3));
                model.setNotificationTopic(cursor.getString(4));
                model.setShowmessage(cursor.getInt(5));
                // Adding contact to list
                entryList.add(model);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        // return contact list
        return entryList;
    }

    // Getting document Count
    public int getDocumentsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

    //Update Entry
    public int updateEntry(models_userNotifications entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TIMESTAMP, entry.getTimestamp());
        values.put(NOTE_TITLE, entry.getNotificationTitle());
        values.put(NOTE_MESSAGE, entry.getNotificationMessage());
        values.put(NOTE_TOPIC, entry.getNotificationTopic());
        values.put(NOTE_SHOW_MESSAGE, entry.getShowmessage());

        // updating row
        return db.update(TABLE_NOTE, values, NOTE_KEY_ID + " = ?",
                new String[] { String.valueOf(models_userNotifications.getMessageID()) });
    }


}