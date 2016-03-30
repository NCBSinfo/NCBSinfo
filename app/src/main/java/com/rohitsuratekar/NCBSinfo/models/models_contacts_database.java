package com.rohitsuratekar.NCBSinfo.models;

public class models_contacts_database {


    private int contact_id;
    String contact_name;
    String contact_department;
    String contact_position;
    String contact_extension;
    String contact_favorite;


    public models_contacts_database(int contact_id, String contact_name, String contact_department, String contact_position, String contact_extension, String contact_favorite) {

        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_department = contact_department;
        this.contact_position = contact_position;
        this.contact_extension = contact_extension;
        this.contact_favorite = contact_favorite;
    }

    public models_contacts_database() {

    }


    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_department() {
        return contact_department;
    }

    public void setContact_department(String contact_department) {
        this.contact_department = contact_department;
    }

    public String getContact_position() {
        return contact_position;
    }

    public void setContact_position(String contact_position) {
        this.contact_position = contact_position;
    }

    public String getContact_extension() {
        return contact_extension;
    }

    public void setContact_extension(String contact_extension) {
        this.contact_extension = contact_extension;
    }

    public String getContact_favorite() {
        return contact_favorite;
    }

    public void setContact_favorite(String contact_favorite) {
        this.contact_favorite = contact_favorite;
    }

}