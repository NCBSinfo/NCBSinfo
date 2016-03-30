
package com.rohitsuratekar.NCBSinfo.retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class retro_MakeQuery {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private retro_Data data;

    public String getTo() {
        return to;
    }

     public void setTo(String to) {
        this.to = to;
    }

    public retro_Data getData() {
        return data;
    }

    public void setData(retro_Data data) {
        this.data = data;
    }

}
