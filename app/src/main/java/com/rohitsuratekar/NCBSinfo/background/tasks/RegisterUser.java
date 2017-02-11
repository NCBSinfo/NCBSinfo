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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UserDetails;
import com.rohitsuratekar.NCBSinfo.background.services.RouteSyncService;
import com.rohitsuratekar.NCBSinfo.background.services.SyncJobs;
import com.rohitsuratekar.NCBSinfo.background.services.UserPreferenceService;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUser extends AsyncTask<Object, Integer, Void> {

    private OnDataRetrieved retrieved;
    private LoginSessionObject sessionObject;
    private AppPrefs prefs;


    public RegisterUser(OnDataRetrieved retrieved) {
        this.retrieved = retrieved;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        retrieved.updateDialog(20, "Creating new user...");
    }


    @Override
    protected Void doInBackground(Object... params) {
        sessionObject = (LoginSessionObject) params[0];
        prefs = new AppPrefs(sessionObject.getContext());
        Log.inform("Registration Process started");
        register();
        return null;
    }

    private void finishSaving() {

        // Start SyncServices
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(sessionObject.getContext()));
        Job myJob = dispatcher.newJobBuilder()
                .setService(SyncJobs.class)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(0, 1))
                .setTag(SyncJobs.RESET_ALL_JOBS)
                .build();
        dispatcher.mustSchedule(myJob);

        Intent intent = new Intent(sessionObject.getContext(), RouteSyncService.class);
        intent.setAction(RouteSyncService.SYNC_ALL);
        sessionObject.getContext().startService(intent);

        //Subscribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic(LoginUser.PUBLIC_TOPIC);
        retrieved.onTaskComplete();
    }

    private void register() {
        sessionObject.getmAuth().createUserWithEmailAndPassword(sessionObject.getEmail(), sessionObject.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    retrieved.updateDialog(40, "Setting up default options...");
                    prefs.setUserEmail(sessionObject.getEmail());
                    prefs.setUserName(sessionObject.getName());
                    prefs.userLoggedIn();

                    sessionObject.getmAuth().getCurrentUser().getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                saveNewData(sessionObject.getmAuth().getCurrentUser().getUid(),
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

    private void saveNewData(String uid, String token) {

        UserDetails user = getPrefUser(uid);
        new RetrofitCalls().synUserPreference(user, token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    Log.inform("Successfully created new user data!");
                    finishSaving();
                } else {
                    retrieved.showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                retrieved.showError(t.getLocalizedMessage());
            }
        });

    }

    private UserDetails getPrefUser(String uid) {
        String syncTime = General.timeStamp();
        //Update Shared preferences
        prefs.setLastSync(syncTime);
        UserDetails user = new UserDetails();
        user.setName(prefs.getUsername());
        user.setEmail(prefs.getUserEmail());
        user.setAccountCreationDate(syncTime);
        user.setLastLogin(syncTime);
        user.setUid(uid);
        user.setFcmToken(FirebaseInstanceId.getInstance().getToken());
        user.setLastSync(syncTime);
        user.setFavoriteType(prefs.getFavoriteType().toLowerCase());
        user.setFavoriteOrigin(prefs.getFavoriteOrigin().toLowerCase());
        user.setFavoriteDestination(prefs.getFavoriteDestination().toLowerCase());
        user.setMigrationID(UserPreferenceService.CURRENT_MIGRATION_ID);
        prefs.setMigrationID(UserPreferenceService.CURRENT_MIGRATION_ID);
        if (prefs.isNotificationAllowed()) {
            user.setNotifications("ON");
        } else {
            user.setNotifications("OFF");
        }
        return user;

    }

    public interface OnDataRetrieved {

        void updateDialog(int progress, String message);

        void showError(String message);

        void onTaskComplete();
    }
}
