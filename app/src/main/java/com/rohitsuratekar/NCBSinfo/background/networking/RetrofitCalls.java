package com.rohitsuratekar.NCBSinfo.background.networking;

import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UpdateMigrationID;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UserDetails;
import com.rohitsuratekar.NCBSinfo.background.services.UserPreferenceService;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCalls {

    private static final String BASE_URL = "https://ncbs-info.firebaseio.com/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new Helper().getClient())
                .build();
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
                + "_" + model.getType().toString().toLowerCase()
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
        id.setMigrationID(UserPreferenceService.CURRENT_MIGRATION_ID);
        return builder().create(FirebaseServices.class).updateMigration(uid, id, token);
    }

}
