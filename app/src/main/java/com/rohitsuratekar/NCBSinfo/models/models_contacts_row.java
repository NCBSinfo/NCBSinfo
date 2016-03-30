package com.rohitsuratekar.NCBSinfo.models;

/**
 * Created by Dexter on 3/16/2016.
 */

public class models_contacts_row {
    private String mText1;
    private String mText2;
    private String mText3;
    private int mID;

    public models_contacts_row(String text1, String text2, String text3, int ID){
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mID = ID;

    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getmText3() {
        return mText3;
    }

    public void setmText3(String mText3) {
        this.mText3 = mText3;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mIDs) {
        this.mID = mIDs;
    }


}