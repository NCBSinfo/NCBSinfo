package com.rohitsuratekar.NCBSinfo.models;

public class ContactModel {
    private int id;
    private String name;
    private String department;
    private String position;
    private String extension;
    private String favorite;

    public ContactModel(int id, String name, String department, String position, String extension, String favorite) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.extension = extension;
        this.favorite = favorite;
    }

    public ContactModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
