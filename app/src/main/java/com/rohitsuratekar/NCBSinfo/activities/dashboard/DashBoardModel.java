package com.rohitsuratekar.NCBSinfo.activities.dashboard;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 16-07-16.
 */
public class DashBoardModel {
    String fieldName;
    String fieldValue;
    int icon;
    boolean editable;

    public DashBoardModel(String fieldName, String fieldValue, int icon, boolean editable) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.icon = icon;
        this.editable = editable;
    }

    public DashBoardModel() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
