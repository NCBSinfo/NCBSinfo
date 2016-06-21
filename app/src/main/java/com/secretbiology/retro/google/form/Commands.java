package com.secretbiology.retro.google.form;

import com.rohitsuratekar.NCBSinfo.background.NetworkConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Commands {

    @FormUrlEncoded
    @POST(NetworkConstants.form.registration.REGISTRATION_URL)
    Call<ResponseBody> submitRegistration(@Field(NetworkConstants.form.registration.NAME) String name,
                                          @Field(NetworkConstants.form.registration.EMAIL) String email,
                                          @Field(NetworkConstants.form.registration.TOKEN) String token,
                                          @Field(NetworkConstants.form.registration.FIREBASE_ID) String firebaseID,
                                          @Field(NetworkConstants.form.registration.EXTERNAL_CODE) String ExternalCode,
                                          @Field("submit") String submit);
}
