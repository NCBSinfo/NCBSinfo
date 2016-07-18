package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsData {

    //Public constants
    //Do not change names. If you want, create new table and copy data from here
    public static final String TABLE_CONTACTS = "table_contacts";
    public static final String CONTACT_KEY_ID = "contact_id";
    public static final String CONTACT_KEY_NAME = "contact_name";
    public static final String CONTACT_KEY_DEPARTMENT = "contact_department";
    public static final String CONTACT_KEY_POSITION = "contact_position";
    public static final String CONTACT_KEY_EXTENSION = "contact_extension";
    public static final String CONTACT_KEY_FAVORITE = "contact_favorite";

    SQLiteDatabase db;

    public ContactsData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public void add(ContactModel contact) {

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_NAME, contact.getName()); // Contact Name
        values.put(CONTACT_KEY_DEPARTMENT, contact.getDepartment()); // Contact Department
        values.put(CONTACT_KEY_POSITION, contact.getPosition()); // Contact Position
        values.put(CONTACT_KEY_EXTENSION, contact.getExtension()); // Contact extension
        values.put(CONTACT_KEY_FAVORITE, contact.getFavorite()); // Contact favorite
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Getting single contact
    public ContactModel get(int id) {
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { CONTACT_KEY_ID, CONTACT_KEY_NAME, CONTACT_KEY_DEPARTMENT, CONTACT_KEY_POSITION, CONTACT_KEY_EXTENSION, CONTACT_KEY_FAVORITE }, CONTACT_KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();

        }
        ContactModel contact = new ContactModel(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        cursor.close();
        return contact;
    }

    // Getting All Contacts
    public List<ContactModel> getAll() {
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setDepartment(cursor.getString(2));
                contact.setPosition(cursor.getString(3));
                contact.setExtension(cursor.getString(4));
                contact.setFavorite(cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return contactList;
    }

    // Updating single contact
    public int update(ContactModel contact) {

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_NAME, contact.getName()); // Contact Name
        values.put(CONTACT_KEY_DEPARTMENT, contact.getDepartment()); // Contact Department
        values.put(CONTACT_KEY_POSITION, contact.getPosition()); // Contact Position
        values.put(CONTACT_KEY_EXTENSION, contact.getExtension()); // Contact extension
        values.put(CONTACT_KEY_FAVORITE, contact.getFavorite()); // Contact favorite
        int TempInt = db.update(TABLE_CONTACTS, values, CONTACT_KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
        return TempInt;
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS);
        db.close();
    }

    // Deleting single contact
    public void delete(ContactModel contact) {
        db.delete(TABLE_CONTACTS, CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


}
