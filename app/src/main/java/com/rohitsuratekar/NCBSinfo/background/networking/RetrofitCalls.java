package com.rohitsuratekar.NCBSinfo.background.networking;

import java.util.HashMap;

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

    private static final String OLD_URL = "https://ncbs-info.firebaseio.com/";
    private static final String BASE_URL = "https://firestore.googleapis.com/v1beta1/projects/ncbs-info/databases/";

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


    public Call<ResponseBody> deleteOld(String email, String token) {
        return builder().create(FirebaseServices.class).deleteOld(email.replace("@", "_").replace(".", "_"), token);
    }

    public Call<ResponseBody> deleteAuth(String uid, String token) {
        return builder().create(FirebaseServices.class).deleteAuth(uid, token);
    }

    public Call<ResponseBody> updateRoute() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Some", "value");
        return builder().create(FirebaseServices.class).updateRoutes("some");
    }


}