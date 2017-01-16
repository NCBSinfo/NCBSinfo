package com.rohitsuratekar.NCBSinfo.database.models;

public class ContactModel {

    private int key;
    private String name;
    private String number;
    private String author;
    private int icon;
    private String createTimestamp;
    private String modifiedTimestamp;
    private boolean isOnFrontPage;


    public ContactModel() {
    }

    public ContactModel(int key, String name, String number, String author, int icon, String createTimestamp, String modifiedTimestamp, boolean isOnFrontPage) {
        this.key = key;
        this.name = name;
        this.number = number;
        this.author = author;
        this.icon = icon;
        this.createTimestamp = createTimestamp;
        this.modifiedTimestamp = modifiedTimestamp;
        this.isOnFrontPage = isOnFrontPage;
    }

    public ContactModel(String name, String number, int icon) {
        this.name = name;
        this.number = number;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(String modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public boolean isOnFrontPage() {
        return isOnFrontPage;
    }

    public void setOnFrontPage(boolean onFrontPage) {
        isOnFrontPage = onFrontPage;
    }
}
