package com.rohitsuratekar.NCBSinfo;

/**
 * Created by Dexter on 1/30/2016.
 */
public class DataObject {
    private String mText1;
    private String mText2;
    private int mID;

    DataObject (String text1, String text2, int ID){
        mText1 = text1;
        mText2 = text2;
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

    public int getmID() {
        return mID;
    }

    public void setmID(int mIDs) {
        this.mID = mIDs;
    }
}