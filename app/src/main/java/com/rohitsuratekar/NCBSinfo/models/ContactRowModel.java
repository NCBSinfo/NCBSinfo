package com.rohitsuratekar.NCBSinfo.models;

/**
 * Created by Rohit Suratekar on 23-04-16.
 */
public class ContactRowModel {

    private String name;
    private String nuber;
    private String department;
    private int id;

    public ContactRowModel(String name, String nuber, String department, int id) {
        this.name = name;
        this.nuber = nuber;
        this.department = department;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNuber() {
        return nuber;
    }

    public void setNuber(String nuber) {
        this.nuber = nuber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
