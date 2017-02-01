package com.rohitsuratekar.NCBSinfo.background.networking;

import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FirebaseServices {

    @GET("newUsers/{user}.json")
    Call<PersonalDetails> getPersonalDetails(@Path("user") String user,
                                             @Query("auth") String token);

    @PATCH("newUsers/{user}.json")
    Call<PersonalDetails> updateUser(@Path("user") String user,
                                     @Query("auth") String token,
                                     @Body PersonalDetails value);

    //Use ResponseBody when you have single array of objects in your json.
    //In your callback, cast this to HashMap<String, Class>
    //Don't try to use List<Class>, it will just give you frustration
    @GET("routeInfo/{uid}/.json")
    Call<ResponseBody> getRouteInfo(@Path("uid") String user,
                                    @Query("auth") String token);

    @PATCH("routeInfo/{uid}/{route}.json")
    Call<RouteModel> updateRoutes(@Path("uid") String user,
                                  @Path("route") String route,
                                  @Body RouteModel value,
                                  @Query("auth") String token);

    @PATCH("routeInfo/users/{uid}.json")
    Call<HashMap<String, RouteModel>> syncRoutes(@Path("uid") String user,
                                                 @Body HashMap<String, RouteModel> value,
                                                 @Query("auth") String token);

    @PATCH("routeInfo/counts/{route}.json")
    Call<CounterDetails> updateCounter(@Path("route") String user,
                                       @Body CounterDetails value,
                                       @Query("auth") String token);
}
