package com.rohitsuratekar.NCBSinfo.background.networking;

import com.rohitsuratekar.NCBSinfo.background.networking.models.OldDetails;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UpdateMigrationID;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UserDetails;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface FirebaseServices {

    @GET("newUsers/{user}.json")
    Call<OldDetails> getOldDetails(@Path("user") String user,
                                   @Query("auth") String token);

    @PATCH("newUsers/{user}.json")
    Call<OldDetails> updateUser(@Path("user") String user,
                                @Query("auth") String token,
                                @Body OldDetails value);

    //Use ResponseBody when you have single array of objects in your json.
    //In your callback, cast this to HashMap<String, Class>
    //Don't try to use List<Class>, it will just give you frustration
    @GET("shiftedUsers/users/{uid}/routes.json")
    Call<ResponseBody> getRouteInfo(@Path("uid") String user,
                                    @Query("auth") String token);

    @PATCH("shiftedUsers/users/{uid}/routes/{route}.json")
    Call<RouteModel> updateRoutes(@Path("uid") String user,
                                  @Path("route") String route,
                                  @Body RouteModel value,
                                  @Query("auth") String token);

    //Sync should be put because it should replace every other route
    @PUT("shiftedUsers/users/{uid}/routes.json")
    Call<HashMap<String, RouteModel>> syncRoutes(@Path("uid") String user,
                                                 @Body HashMap<String, RouteModel> value,
                                                 @Query("auth") String token);


    @PATCH("shiftedUsers/users/{uid}.json")
    Call<UserDetails> syncUserPreference(@Path("uid") String uid,
                                         @Body UserDetails value,
                                         @Query("auth") String token);

    @GET("shiftedUsers/users/{uid}.json")
    Call<UserDetails> getUserInfo(@Path("uid") String user,
                                  @Query("auth") String token);

    @DELETE("newUsers/{user}.json")
    Call<ResponseBody> deleteOld(@Path("user") String uid,
                                 @Query("auth") String token);

    @DELETE("authEmails/{user}.json")
    Call<ResponseBody> deleteAuth(@Path("user") String uid,
                                  @Query("auth") String token);

    @PATCH("shiftedUsers/users/{uid}.json")
    Call<UpdateMigrationID> updateMigration(@Path("uid") String uid,
                                            @Body UpdateMigrationID value,
                                            @Query("auth") String token);

}
