package com.rohitsuratekar.retro.google.gcm;


import com.rohitsuratekar.retro.google.gcm.reponse.MakeQuery;
import com.rohitsuratekar.retro.google.gcm.reponse.Topic;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Commands {

    @POST("/gcm/send")
    Call<Topic> sendTopicMessage (@Body MakeQuery data);

}
