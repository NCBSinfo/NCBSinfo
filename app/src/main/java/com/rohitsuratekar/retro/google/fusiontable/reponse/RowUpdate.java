package com.rohitsuratekar.retro.google.fusiontable.reponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RowUpdate {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("columns")
    @Expose
    private List<String> columns = new ArrayList<String>();
    @SerializedName("rows")
    @Expose
    private List<List<String>> rows = new ArrayList<List<String>>();

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

}