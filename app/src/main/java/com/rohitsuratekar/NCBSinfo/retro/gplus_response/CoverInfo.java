
package com.rohitsuratekar.NCBSinfo.retro.gplus_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoverInfo {

    @SerializedName("topImageOffset")
    @Expose
    private Integer topImageOffset;
    @SerializedName("leftImageOffset")
    @Expose
    private Integer leftImageOffset;

    /**
     * 
     * @return
     *     The topImageOffset
     */
    public Integer getTopImageOffset() {
        return topImageOffset;
    }

    /**
     * 
     * @param topImageOffset
     *     The topImageOffset
     */
    public void setTopImageOffset(Integer topImageOffset) {
        this.topImageOffset = topImageOffset;
    }

    /**
     * 
     * @return
     *     The leftImageOffset
     */
    public Integer getLeftImageOffset() {
        return leftImageOffset;
    }

    /**
     * 
     * @param leftImageOffset
     *     The leftImageOffset
     */
    public void setLeftImageOffset(Integer leftImageOffset) {
        this.leftImageOffset = leftImageOffset;
    }

}
