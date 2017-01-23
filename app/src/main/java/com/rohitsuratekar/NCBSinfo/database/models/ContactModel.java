package com.rohitsuratekar.NCBSinfo.database.models;

/**
 * Created by Dexter on 20-01-2017.
 */

public class ContactModel {

    private String name;
    private String number;
    private int icon;
    private int color;
    private int darkColor;
    private String email;


    public ContactModel(String name, String number, int icon) {
        this.name = name;
        this.number = number;
        this.icon = icon;
    }

    public ContactModel() {
    }

    public ContactModel(String name, String number, int icon, int color) {
        this.name = name;
        this.number = number;
        this.icon = icon;
        this.color = color;
        this.darkColor = color;
    }

    public ContactModel(String name, String number, int icon, int color[]) {
        this.name = name;
        this.number = number;
        this.icon = icon;
        this.color = color[0];
        this.darkColor = color[1];
    }

    public void setBothColors(int[] both) {
        this.color = both[0];
        this.darkColor = both[1];
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDarkColor() {
        return darkColor;
    }

    public void setDarkColor(int darkColor) {
        this.darkColor = darkColor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
