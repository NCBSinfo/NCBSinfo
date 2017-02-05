package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UserDetails;
import com.rohitsuratekar.NCBSinfo.background.services.RouteSyncService;
import com.rohitsuratekar.NCBSinfo.background.services.SyncJobs;
import com.rohitsuratekar.NCBSinfo.background.services.UserPreferenceService;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginUser extends AsyncTask<Object, Integer, Void> {

    private static final String PUBLIC_TOPIC = "public";

    private OnDataRetrieved retrieved;
    private LoginSessionObject sessionObject;
    private AppPrefs prefs;
    private Gson gson = new Gson();

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
                                        getAllUserData(sessionObject.getmAuth().getCurrentUser().getUid(),
                                                task.getResult().getToken());
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

    private void getAllUserData(final String uid, final String token) {
        new RetrofitCalls().getUserDetails(uid, token)
                .enqueue(new Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, retrofit2.Response<UserDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                UserDetails u = response.body();
                                prefs.setUserName(u.getName());
                                prefs.userLoggedIn();
                                prefs.setFavoriteOrigin(u.getFavoriteOrigin());
                                prefs.setFavoriteDestination(u.getFavoriteDestination());
                                prefs.setFavoriteType(Helper.getType(u.getFavoriteType()));
                                prefs.setMigrationID(u.getMigrationID());
                                if (u.getNotifications() != null &&
                                        u.getNotifications().toLowerCase().equals("off")) {
                                    prefs.notificationAllowed(false);
                                } else {
                                    prefs.notificationAllowed(true);
                                }
                                saveData(u.getRoutes());
                            } else {
                                Intent i = new Intent(sessionObject.getContext(), UserPreferenceService.class);
                                i.setAction(UserPreferenceService.OLD_USER_WITH_NO_DATA);
                                sessionObject.getContext().startService(i);
                                finishExecution();
                            }
                        } else {
                            retrieved.showError("Oh, no. Something went wrong :( Please try again.");

                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        retrieved.showError(t.getLocalizedMessage());
                    }
                });
    }


    private void saveData(Map<String, Object> map) {
        if (map != null) {
            //If there is data on server, replace this with existing
            if (map.values().size() != 0) {
                new RouteData(sessionObject.getContext()).clearAll();
            }
            for (Object object : map.values()) {
                RouteModel r = gson.fromJson(gson.toJson(object), RouteModel.class);
                new RouteData(sessionObject.getContext()).add(r);
            }
            new RouteData(sessionObject.getContext()).updateSyncTime(General.timeStamp());
            int favRoute = new RouteData(sessionObject.getContext()).checkIfExistsRoute(
                    prefs.getFavoriteOrigin(), prefs.getFavoriteDestination(), Helper.getType(prefs.getFavoriteType()));
            if (favRoute != -1) {
                prefs.setFavoriteRoute(favRoute);
            }
            loadRoutes.execute(sessionObject.getContext());
        } else {
            //There is no route data available on the server, send request to sync existing data
            Log.inform("No Route Data Found, sending request to sync current");
            Intent intent = new Intent(sessionObject.getContext(), RouteSyncService.class);
            intent.setAction(RouteSyncService.SYNC_ALL);
            sessionObject.getContext().startService(intent);
            finishExecution();
        }
    }

    private LoadRoutes loadRoutes = new LoadRoutes(new OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {
            finishExecution();
        }
    });

    private void finishExecution() {

        prefs.setLastSync(General.timeStamp());
        Intent intent = new Intent(sessionObject.getContext(), UserPreferenceService.class);
        intent.setAction(UserPreferenceService.DELETE_OLD);
        sessionObject.getContext().startService(intent);

        // Start SyncServices
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(sessionObject.getContext()));
        Job myJob = dispatcher.newJobBuilder()
                .setService(SyncJobs.class)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(0, 1))
                .setTag(SyncJobs.RESET_ALL_JOBS)
                .build();
        dispatcher.mustSchedule(myJob);

        //Login Sync
        Intent login = new Intent(sessionObject.getContext(), UserPreferenceService.class);
        login.setAction(UserPreferenceService.SYNC_LOGIN);
        sessionObject.getContext().startService(login);

        //Subscribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic(PUBLIC_TOPIC);

        retrieved.onTaskComplete();

    }


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
