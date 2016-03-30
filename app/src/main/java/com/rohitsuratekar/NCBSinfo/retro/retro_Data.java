
package com.rohitsuratekar.NCBSinfo.retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class retro_Data {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rcode")
    @Expose
    private String rcode;

    @SerializedName("value")
    @Expose
    private String value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
