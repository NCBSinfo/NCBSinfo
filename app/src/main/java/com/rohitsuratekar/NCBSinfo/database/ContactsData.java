package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.secretbiology.helpers.general.sql.Column;
import com.secretbiology.helpers.general.sql.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ContactsData {
    //Public constants
    //Do not change names. If you want, create new table and copy data from here
    public static final String TABLE_CONTACTS = "table_contacts";
    public static final String ID = "contact_id";
    public static final String NAME = "contact_name";
    public static final String DEPARTMENT = "contact_department";
    public static final String POSITION = "contact_position";
    public static final String EXTENSION = "contact_extension";
    public static final String FAVORITE = "contact_favorite";

    SQLiteDatabase db;
    Database database;
    private Table contactTable;
    private LinkedHashMap<String, Column> map;

    public ContactsData(Context context) {
        this.database = Database.getInstance(context);
        this.db = database.openDatabase();
        contactTable = new Table(db, TABLE_CONTACTS, getColumn());
        map = contactTable.getMap();
    }

    private static List<Column> getColumn() {
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(ID, Column.ColumnType.PRIMARY_INTEGER));
        columnList.add(new Column(NAME, Column.ColumnType.TEXT));
        columnList.add(new Column(DEPARTMENT, Column.ColumnType.TEXT));
        columnList.add(new Column(POSITION, Column.ColumnType.TEXT));
        columnList.add(new Column(EXTENSION, Column.ColumnType.TEXT));
        columnList.add(new Column(FAVORITE, Column.ColumnType.TEXT));
        return columnList;
    }


    public static void makeTable(SQLiteDatabase db) {
        Table contactTable = new Table(db, TABLE_CONTACTS, getColumn());
        contactTable.make();
    }

    public void add(ContactModel contact) {
        contactTable.addRow(putValues(contact));
        database.closeDatabase();
    }

    // Getting single contact
    public ContactModel get(int id) {
        LinkedHashMap<String, Column> m = contactTable.getRowByValue(ID, id).getMap();
        ContactModel contact = getValues(m);
        database.closeDatabase();
        return contact;
    }

    // Getting All Contacts
    public List<ContactModel> getAll() {
        List<ContactModel> contactList = new ArrayList<>();
        for (LinkedHashMap<String, Column> m : contactTable.getAllRows()) {
            contactList.add(getValues(m));
        }

        database.closeDatabase();
        return contactList;
    }

    // Updating single contact
    public int update(ContactModel contact) {
        int TempInt = (int) contactTable.update(putValues(contact), ID);
        database.closeDatabase();
        return TempInt;
    }

    // Delete all data
    public void clearAll() {
        contactTable.clearAll();
        database.closeDatabase();
    }

    // Deleting single contact
    public void delete(ContactModel contact) {
        contactTable.delete(ID, contact.getId());
        database.closeDatabase();
    }

    // Drop
    public void drop() {
        contactTable.drop();
        database.closeDatabase();
    }

    private Column withValue(String columnName, Object value) {
        Column column = map.get(columnName);
        column.setData(value);
        return column;
    }

    private List<Column> putValues(ContactModel contact) {
        List<Column> newList = new ArrayList<>();
        newList.add(withValue(ID, contact.getId()));
        newList.add(withValue(NAME, contact.getName()));
        newList.add(withValue(DEPARTMENT, contact.getDepartment()));
        newList.add(withValue(POSITION, contact.getPosition()));
        newList.add(withValue(EXTENSION, contact.getExtension()));
        newList.add(withValue(FAVORITE, contact.getFavorite()));
        return newList;
    }

    private ContactModel getValues(LinkedHashMap<String, Column> m) {
        ContactModel contact = new ContactModel();
        contact.setId((Integer) m.get(ID).getData());
        contact.setDepartment((String) m.get(DEPARTMENT).getData());
        contact.setExtension((String) m.get(EXTENSION).getData());
        contact.setName((String) m.get(NAME).getData());
        contact.setFavorite((String) m.get(FAVORITE).getData());
        contact.setPosition((String) m.get(POSITION).getData());
        return contact;
    }

}
