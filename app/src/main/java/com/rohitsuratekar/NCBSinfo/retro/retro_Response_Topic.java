package com.rohitsuratekar.NCBSinfo.retro;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class retro_Response_Topic {

    @SerializedName("message_id")
    @Expose
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}