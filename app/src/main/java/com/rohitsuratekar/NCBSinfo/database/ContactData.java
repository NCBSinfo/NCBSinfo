package com.rohitsuratekar.NCBSinfo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactData {

    public void addContact(SQLiteDatabase db, ContactModel contact) {

        ContentValues values = new ContentValues();
        values.put(SQL.CONTACT_KEY_NAME, contact.getName()); // Contact Name
        values.put(SQL.CONTACT_KEY_DEPARTMENT, contact.getDepartment()); // Contact Department
        values.put(SQL.CONTACT_KEY_POSITION, contact.getPosition()); // Contact Position
        values.put(SQL.CONTACT_KEY_EXTENSION, contact.getExtension()); // Contact extension
        values.put(SQL.CONTACT_KEY_FAVORITE, contact.getFavorite()); // Contact favorite

        // Inserting Row
        db.insert(SQL.TABLE_CONTACTS, null, values);  //Null is to autoincriment
        db.close(); // Closing database connection
    }

    // Getting single contact
    public ContactModel getContact(SQLiteDatabase db, int id) {

        Cursor cursor = db.query(SQL.TABLE_CONTACTS, new String[] { SQL.CONTACT_KEY_ID, SQL.CONTACT_KEY_NAME, SQL.CONTACT_KEY_DEPARTMENT, SQL.CONTACT_KEY_POSITION, SQL.CONTACT_KEY_EXTENSION, SQL.CONTACT_KEY_FAVORITE }, SQL.CONTACT_KEY_ID + "=?",
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
    public List<ContactModel> getAllContacts(SQLiteDatabase db ) {
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SQL.TABLE_CONTACTS;
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
    public int updateContact(SQLiteDatabase db, ContactModel contact) {

        ContentValues values = new ContentValues();
        values.put(SQL.CONTACT_KEY_NAME, contact.getName()); // Contact Name
        values.put(SQL.CONTACT_KEY_DEPARTMENT, contact.getDepartment()); // Contact Department
        values.put(SQL.CONTACT_KEY_POSITION, contact.getPosition()); // Contact Position
        values.put(SQL.CONTACT_KEY_EXTENSION, contact.getExtension()); // Contact extension
        values.put(SQL.CONTACT_KEY_FAVORITE, contact.getFavorite()); // Contact favorite
        return db.update(SQL.TABLE_CONTACTS, values, SQL.CONTACT_KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(SQLiteDatabase db, ContactModel contact) {
        db.delete(SQL.TABLE_CONTACTS, SQL.CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

}
