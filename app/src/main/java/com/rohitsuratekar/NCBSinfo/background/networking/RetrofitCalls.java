package com.rohitsuratekar.NCBSinfo.background.networking;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class RetrofitCalls {

    public static final String CURRENT_MIGRATION_ID = "v6";
    private static final String BASE_URL = "https://ncbs-info.firebaseio.com/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpClient.addInterceptor(interceptor).build();
    }


    public Call<RouteModel> updateRoute(String uid, String token, RouteModel model) {
        return builder().create(FirebaseServices.class).
                updateRoutes(uid, makeRouteNodeName(model), model, token);
    }

    public Call<ResponseBody> getRouteInfo(String uid, String token) {
        return builder().create(FirebaseServices.class).
                getRouteInfo(uid, token);
    }

    public Call<HashMap<String, RouteModel>> syncRoutes(String uid, List<RouteModel> models, String token) {
        HashMap<String, RouteModel> map = new HashMap<>();
        for (RouteModel r : models) {
            map.put(makeRouteNodeName(r), r);
        }
        return builder().create(FirebaseServices.class).
                syncRoutes(uid, map, token);
    }

    private String makeRouteNodeName(RouteModel model) {
        return model.getOrigin().toLowerCase()
                + "_" + model.getDestination().toLowerCase()
                + "_" + model.getType().toLowerCase()
                + "_" + model.getDay();
    }

    public Call<UserDetails> synUserPreference(UserDetails model, String token) {
        return builder().create(FirebaseServices.class).syncUserPreference(model.getUid(), model, token);
    }

    public Call<UserDetails> getUserDetails(String uid, String token) {
        return builder().create(FirebaseServices.class).getUserInfo(uid, token);
    }

    public Call<ResponseBody> deleteOld(String email, String token) {
        return builder().create(FirebaseServices.class).deleteOld(email.replace("@", "_").replace(".", "_"), token);
    }

    public Call<ResponseBody> deleteAuth(String uid, String token) {
        return builder().create(FirebaseServices.class).deleteAuth(uid, token);
    }

    // Migrate to current ID
    public Call<UpdateMigrationID> updateMigration(String uid, String token) {
        UpdateMigrationID id = new UpdateMigrationID();
        id.setMigrationID(CURRENT_MIGRATION_ID);
        return builder().create(FirebaseServices.class).updateMigration(uid, id, token);
    }


}