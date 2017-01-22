package com.rohitsuratekar.NCBSinfo.database.models;

/**
 * Created by Dexter on 20-01-2017.
 */

public class ContactModel {

    private String name;
    private String number;
    private int icon;

    public ContactModel(String name, String number, int icon) {
        this.name = name;
        this.number = number;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
