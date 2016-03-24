package com.rohitsuratekar.NCBSinfo.retro;



import com.rohitsuratekar.NCBSinfo.retro.gplus_response.Response_gplus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface retro_Commands {

    @POST("/gcm/send")
    Call<retro_Response_Topic> sendTopicMessage (@Body retro_MakeQuery data);

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<retro_Response_FusionTableRow> getAllRows (@Field("sql") String SQLquery,
                                      @Field("key") String Token);

    @GET("/plus/v1/people/me")
    Call<Response_gplus> getUserinfo (@Query("key") String AccessToken);

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<retro_Response_SpecificRowValue> getSpecificRow (@Field("sql") String SQLquery,
                                              @Field("key") String Token);

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<retro_Response_RowUpdate> sendLogrow (@Field("sql") String SQLquery,
                                                          @Field("key") String Token);

}
