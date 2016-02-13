package com.rohitsuratekar.NCBSinfo;
/**
 * Created by Dexter on 1/28/2016.
 */
public class SQfields {

    //private variables
    int _id;
    String _doc_name;
    String _doc_department;
    String _doc_position;
    String _doc_extension;
    String _doc_favorite;

    // Empty constructor
    public SQfields(){

    }
    // constructor
    public SQfields(int id, String contact_name, String contact_department, String doc_username, String contact_extension, String contact_favorite){
        this._id = id;
        this._doc_name = contact_name;
        this._doc_department = contact_department;
        this._doc_position = doc_username;
        this._doc_extension = contact_extension;
        this._doc_favorite = contact_favorite;

    }

    // constructor
  /*  public SQfields(String contact_name, String contact_department, String doc_username, String contact_extension, String contact_favorite){
        this._doc_name = contact_name;
        this._doc_department = contact_department;
        this._doc_position = doc_username;
        this._doc_extension = contact_extension;
        this._doc_favorite = contact_favorite;
    }*/
    // getting ID
    public int getContactID(){
        return this._id;
    }

    // setting id
    public void setContactID(int id){
        this._id = id;
    }

    // getting name
    public String getContactName(){
        return this._doc_name;
    }

    // setting name
    public void setContactName(String name){
        this._doc_name = name;
    }

    // getting number
    public String getContactDepartment(){
        return this._doc_department;
    }

    // setting number
    public void setContactDepartment(String contact_department){
        this._doc_department = contact_department; }

    // getting username
    public String getContactPosition(){
        return this._doc_position;
    }

    // setting username
    public void setContactPosition(String doc_username){
        this._doc_position = doc_username;
    }

    // getting date
    public String getContactExtension(){
        return this._doc_extension;
    }

    // setting date
    public void setContactExtension(String contact_extension){
        this._doc_extension = contact_extension;
    }

    // getting notes
    public String getContactFavorite(){
        return this._doc_favorite;
    }

    // setting notes
    public void setContactFavorite(String doc_note){
        this._doc_favorite = doc_note;
    }

}