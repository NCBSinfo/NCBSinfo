
package com.rohitsuratekar.NCBSinfo.retro.gplus_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlacesLived {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("primary")
    @Expose
    private Boolean primary;

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 
     * @return
     *     The primary
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * 
     * @param primary
     *     The primary
     */
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

}
