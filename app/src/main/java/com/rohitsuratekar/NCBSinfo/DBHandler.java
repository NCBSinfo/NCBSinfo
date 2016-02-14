package com.rohitsuratekar.NCBSinfo;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ContactStore2";

    // Contacts table name
    private static final String TABLE_DOCS = "new_contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "contact_name";
    private static final String KEY_DEPARTMENT = "contact_department";
    private static final String KEY_POSITION = "contact_position";
    private static final String KEY_EXTENSION = "contact_extension";
    private static final String KEY_FAVORITE = "contact_favorite";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DOC_TABLE = "CREATE TABLE " + TABLE_DOCS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DEPARTMENT + " TEXT," + KEY_POSITION + " TEXT," + KEY_EXTENSION + " TEXT," +  KEY_FAVORITE + " TEXT " + ")";
        db.execSQL(CREATE_DOC_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);

        // Create tables again
        onCreate(db);
    }

    //Operations
    //Operations

    // Adding new document
    void addContact(SQfields contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getContactName()); // Document Name
        values.put(KEY_DEPARTMENT, contact.getContactDepartment()); // Document No
        values.put(KEY_POSITION, contact.getContactPosition()); // Document Username
        values.put(KEY_EXTENSION, contact.getContactExtension()); // Document Date
        values.put(KEY_FAVORITE, contact.getContactFavorite()); // Document Notes

        // Inserting Row
        db.insert(TABLE_DOCS, null, values);  //Null is to autoincriment
        db.close(); // Closing database connection
    }

    // Getting single Document
    SQfields getDocument(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DOCS, new String[] { KEY_ID, KEY_NAME, KEY_DEPARTMENT, KEY_POSITION, KEY_EXTENSION, KEY_FAVORITE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();

        }
        SQfields document = new SQfields(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        cursor.close();
        return document;
    }

    // Getting All Contacts
    public List<SQfields> getAllDocuments() {
        List<SQfields> documentList = new ArrayList<SQfields>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOCS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SQfields document = new SQfields();
                document.setContactID(Integer.parseInt(cursor.getString(0)));
                document.setContactName(cursor.getString(1));
                document.setContactDepartment(cursor.getString(2));
                document.setContactPosition(cursor.getString(3));
                document.setContactExtension(cursor.getString(4));
                document.setContactFavorite(cursor.getString(5));
                // Adding contact to list
                documentList.add(document);
            } while (cursor.moveToNext());
        }

        // return contact list
        return documentList;
    }

    // Updating single document
    public int updateDocument(SQfields document) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, document.getContactName());
        values.put(KEY_DEPARTMENT, document.getContactDepartment());
        values.put(KEY_POSITION, document.getContactPosition());
        values.put(KEY_EXTENSION, document.getContactExtension());
        values.put(KEY_FAVORITE, document.getContactFavorite());

        // updating row
        return db.update(TABLE_DOCS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(document.getContactID()) });
    }

    // Deleting single document
    public void deleteDocument(SQfields document) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCS, KEY_ID + " = ?", new String[] { String.valueOf(document.getContactID()) });
        db.close();

    }


    // Getting document Count
    public int getDocumentsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DOCS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}