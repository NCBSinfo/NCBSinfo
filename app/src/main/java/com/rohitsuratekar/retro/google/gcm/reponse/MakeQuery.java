
package com.rohitsuratekar.retro.google.gcm.reponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeQuery {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getTo() {
        return to;
    }

     public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
