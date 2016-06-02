package com.rohitsuratekar.retro.google.form;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Dexter on 19-03-16.
 */
public interface Commands {

    @FormUrlEncoded
    @POST("forms/d/1RMKGajIAZ6xDmlT3GB0ndJQraUyjpBswnGqjSrOszAM/formResponse")
    Call<ResponseBody> submitForm  (@Field("entry_309813602") String GroupID,
                                    @Field("entry_1593460908") String email,
                                    @Field("entry_845724782") String tokensting,
                                    @Field("entry_1745980777") String topicCode,
                                    @Field("submit") String submit);

    @FormUrlEncoded
    @POST("forms/d/1wdTuzR0R_H7MBbgaraqH9rI68zTjE1EL6hlY29dCEwc/formResponse")
    Call<ResponseBody> submitExternalForm  (@Field("entry_675707263") String name,
                                    @Field("entry_1261125575") String email,
                                    @Field("entry_1147357417") String tokensting,
                                    @Field("entry_1750062233") String ExternalCode,
                                    @Field("submit") String submit);

}
