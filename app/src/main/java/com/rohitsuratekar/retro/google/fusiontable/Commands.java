package com.rohitsuratekar.retro.google.fusiontable;

import com.rohitsuratekar.retro.google.fusiontable.reponse.FusionTableRow;
import com.rohitsuratekar.retro.google.fusiontable.reponse.RowUpdate;
import com.rohitsuratekar.retro.google.fusiontable.reponse.SpecificRowValue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Commands {

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<FusionTableRow> getAllRows (@Field("sql") String SQLquery,
                                      @Field("key") String Token);

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<SpecificRowValue> getSpecificRow (@Field("sql") String SQLquery,
                                              @Field("key") String Token);

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<RowUpdate> sendLogrow (@Field("sql") String SQLquery,
                                                          @Field("key") String Token);

    @GET("fusiontables/v2/query")
    Call<FusionTableRow> getPublicRows (@Query("sql") String SQLquery,
                                        @Query("key")String key);

}
