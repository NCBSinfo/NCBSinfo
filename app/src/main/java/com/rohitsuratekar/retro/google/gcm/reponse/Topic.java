package com.rohitsuratekar.retro.google.gcm.reponse;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Topic {

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