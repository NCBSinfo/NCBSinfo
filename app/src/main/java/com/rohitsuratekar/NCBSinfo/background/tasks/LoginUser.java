package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.background.networking.PersonalDetails;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.services.RouteSyncService;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUser extends AsyncTask<Object, Integer, Void> {

    private OnDataRetrieved retrieved;
    private LoginSessionObject sessionObject;
    private AppPrefs prefs;
    private int defaultRoute;

    public LoginUser(OnDataRetrieved retrieved) {
        this.retrieved = retrieved;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        retrieved.updateDialog(20, "Signing in...");
    }

    @Override
    protected Void doInBackground(Object... params) {
        sessionObject = (LoginSessionObject) params[0];
        prefs = new AppPrefs(sessionObject.getContext());
        Log.inform("Login Process started");
        login();
        return null;
    }


    private void login() {
        sessionObject.getmAuth().signInWithEmailAndPassword(sessionObject.getEmail(), sessionObject.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            retrieved.updateDialog(40, "Loading data");
                            prefs.setUserEmail(sessionObject.getEmail());
                            prefs.userLoggedIn();
                            sessionObject.getmAuth().getCurrentUser().getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        getAllUserData(task.getResult().getToken());
                                    } else {
                                        retrieved.showError(task.getException().getLocalizedMessage());
                                    }
                                }
                            });
                        } else {
                            retrieved.showError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private void getAllUserData(final String token) {
        new RetrofitCalls().personalDetailsCall(sessionObject.getEmail(), token)
                .enqueue(new Callback<PersonalDetails>() {
                    @Override
                    public void onResponse(Call<PersonalDetails> call, retrofit2.Response<PersonalDetails> response) {
                        if (response.isSuccessful()) {
                            prefs.setUserName(response.body().getName());
                            defaultRoute = response.body().getDefaultRoute();
                            Log.inform("Default is " + defaultRoute);
                            prefs.setFavoriteRoute(defaultRoute);
                            String uid = sessionObject.getmAuth().getCurrentUser().getUid();
                            getFromServer(uid, token);
                        } else {
                            retrieved.showError("Something went wrong :(");
                        }
                    }

                    @Override
                    public void onFailure(Call<PersonalDetails> call, Throwable t) {
                        retrieved.showError(t.getLocalizedMessage());
                    }
                });
    }


    //Get data from server to compare with modified data
    private void getFromServer(String uid, String token) {
        retrieved.updateDialog(60, "Preparing routes...");
        new RetrofitCalls().getRouteInfo(uid, token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            HashMap<String, RouteModel> map = new HashMap<String, RouteModel>();
                            Type type = new TypeToken<HashMap<String, RouteModel>>() {
                            }.getType();
                            try {
                                map = new Gson().fromJson(response.body().string(), type);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            saveData(map);
                        } else {
                            Log.inform(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void saveData(HashMap<String, RouteModel> map) {
        if (map != null) {
            //If there is data on server, replace this with existing
            if (map.values().size() != 0) {
                new RouteData(sessionObject.getContext()).clearAll();
            }
            for (RouteModel r : map.values()) {
                new RouteData(sessionObject.getContext()).add(r);
            }
            new RouteData(sessionObject.getContext()).updateSyncTime(General.timeStamp());
            loadRoutes.execute(sessionObject.getContext());
        } else {
            //There is no route data available on the server, send request to sync existing data
            Log.inform("No Route Data Found, sending request to sync current");
            Intent intent = new Intent(sessionObject.getContext(), RouteSyncService.class);
            intent.setAction(RouteSyncService.SYNC_ALL);
            sessionObject.getContext().startService(intent);
        }

    }

    private LoadRoutes loadRoutes = new LoadRoutes(new OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {
            new Helper().legacyDefaultConverter(sessionObject.getContext(), prefs.getFavoriteRoute());
            retrieved.onTaskComplete();
        }
    });


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public interface OnDataRetrieved {
        void updateDialog(int progress, String message);

        void showError(String message);

        void onTaskComplete();
    }

}
