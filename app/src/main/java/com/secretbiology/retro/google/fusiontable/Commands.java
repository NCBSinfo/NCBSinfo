package com.secretbiology.retro.google.fusiontable;

import com.secretbiology.retro.google.fusiontable.reponse.RowModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Commands {

    @FormUrlEncoded
    @POST("fusiontables/v2/query")
    Call<RowModel> getRows(@Field("sql") String SQLquery,
                          @Field("key") String Token);

    @GET("fusiontables/v2/query")
    Call<RowModel> getPublicRows(@Query("sql") String SQLquery,
                                 @Query("key") String key);

}
