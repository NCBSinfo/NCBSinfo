package com.rohitsuratekar.NCBSinfo.retro.gform;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Dexter on 19-03-16.
 */
public interface gform_FormCommands {

    @FormUrlEncoded
    @POST("forms/d/1RMKGajIAZ6xDmlT3GB0ndJQraUyjpBswnGqjSrOszAM/formResponse")
    Call<ResponseBody> submitForm  (@Field("entry_309813602") String GroupID,
                                    @Field("entry_1593460908") String email,
                                    @Field("entry_845724782") String tokensting,
                                    @Field("submit") String submit);

}
