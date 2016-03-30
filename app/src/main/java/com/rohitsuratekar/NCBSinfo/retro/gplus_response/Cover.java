
package com.rohitsuratekar.NCBSinfo.retro.gplus_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cover {

    @SerializedName("layout")
    @Expose
    private String layout;
    @SerializedName("coverPhoto")
    @Expose
    private CoverPhoto coverPhoto;
    @SerializedName("coverInfo")
    @Expose
    private CoverInfo coverInfo;

    /**
     * 
     * @return
     *     The layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * 
     * @param layout
     *     The layout
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * 
     * @return
     *     The coverPhoto
     */
    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    /**
     * 
     * @param coverPhoto
     *     The coverPhoto
     */
    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    /**
     * 
     * @return
     *     The coverInfo
     */
    public CoverInfo getCoverInfo() {
        return coverInfo;
    }

    /**
     * 
     * @param coverInfo
     *     The coverInfo
     */
    public void setCoverInfo(CoverInfo coverInfo) {
        this.coverInfo = coverInfo;
    }

}
