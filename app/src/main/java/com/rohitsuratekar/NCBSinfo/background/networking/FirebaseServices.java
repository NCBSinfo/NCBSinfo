package com.rohitsuratekar.NCBSinfo.background.networking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

interface FirebaseServices {


    @DELETE("newUsers/{user}.json")
    Call<ResponseBody> deleteOld(@Path("user") String uid,
                                 @Query("auth") String token);

    @DELETE("authEmails/{user}.json")
    Call<ResponseBody> deleteAuth(@Path("user") String uid,
                                  @Query("auth") String token);


    @GET("users/details/documents/{uid}/")
    Call<ResponseBody> updateRoutes(@Path("uid") String uid);

}