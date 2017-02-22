package com.rohitsuratekar.NCBSinfo.background.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UpdateMigrationID;
import com.rohitsuratekar.NCBSinfo.background.networking.models.UserDetails;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPreferenceService extends IntentService {

    public static final String CURRENT_MIGRATION_ID = "v6";


    public static final String SYNC_USER_PREFERENCES = "syncPreferences";
    public static final String SYNC_LOGIN = "syncLogin";
    public static final String DELETE_OLD = "deleteOld";
    public static final String OLD_USER_WITH_NO_DATA = "oldUserWithNoData";


    public UserPreferenceService(String name) {
        super(name);
    }

    public UserPreferenceService() {
        super("UserPreferenceService");
    }

    private FirebaseAuth mAuth;
    private String uid;
    private String token;
    private AppPrefs prefs;
    private String fireBaseToken;


    @Override
    protected void onHandleIntent(Intent intent) {

        prefs = new AppPrefs(getBaseContext());
        Log.inform("User Preference syncing started");
        mAuth = FirebaseAuth.getInstance();
        final String action = intent.getAction();
        if (mAuth.getCurrentUser() != null && action != null) {

            if (General.isNetworkAvailable(getBaseContext())) {

                mAuth.getCurrentUser().getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            uid = mAuth.getCurrentUser().getUid();
                            fireBaseToken = FirebaseInstanceId.getInstance().getToken();
                            token = task.getResult().getToken();
                            switch (action) {
                                case SYNC_USER_PREFERENCES:
                                    regularSync();
                                    break;
                                case SYNC_LOGIN:
                                    syncLogin();
                                    break;
                                case DELETE_OLD:
                                    if (!prefs.getMigrationId().toLowerCase().equals(CURRENT_MIGRATION_ID)) {
                                        deleteOld();
                                    } else {
                                        Log.inform("Auth deletion aborted because migration has already done");
                                    }
                                    break;
                                case OLD_USER_WITH_NO_DATA:
                                    setOldUserWithNoData();
                                    break;
                            }
                        } else {
                            Log.error(task.getException().getLocalizedMessage());
                        }
                    }
                });
            } else {
                Log.inform("Sync cancelled because of no network");
            }
        } else {
            Log.inform("Sync cancelled because user is not logged in");
        }

    }

    private void regularSync() {
        //TODO: implementing two way sync
        UserDetails user = getPrefUser();
        user.setUid(uid);
        user.setFcmToken(fireBaseToken);
        sync(user);
    }

    private void setOldUserWithNoData() {

        prefs.setMigrationID(CURRENT_MIGRATION_ID);

        UserDetails user = getPrefUser();
        user.setAccountCreationDate(General.timeStamp());
        user.setMigrationID(CURRENT_MIGRATION_ID);
        user.setNotifications("ON");
        user.setLastLogin(General.timeStamp());

        sync(user);
        Intent intent = new Intent(getBaseContext(), RouteSyncService.class);
        intent.setAction(RouteSyncService.SYNC_ALL);
        getBaseContext().startService(intent);

    }

    private void syncLogin() {
        UserDetails user = new UserDetails();
        user.setUid(uid);
        user.setFcmToken(fireBaseToken);
        user.setLastLogin(General.timeStamp());
        prefs.setLastSync(General.timeStamp());
        sync(user);
    }

    private UserDetails getPrefUser() {
        String syncTime = General.timeStamp();
        //Update Shared preferences
        prefs.setLastSync(syncTime);
        UserDetails user = new UserDetails();
        user.setName(prefs.getUsername());
        user.setEmail(prefs.getUserEmail());
        user.setUid(uid);
        user.setFcmToken(fireBaseToken);
        user.setLastSync(syncTime);
        user.setFavoriteType(prefs.getFavoriteType().toLowerCase());
        user.setFavoriteOrigin(prefs.getFavoriteOrigin().toLowerCase());
        user.setFavoriteDestination(prefs.getFavoriteDestination().toLowerCase());
        user.setMigrationID(prefs.getMigrationId());
        if (prefs.isNotificationAllowed()) {
            user.setNotifications("ON");
        } else {
            user.setNotifications("OFF");
        }

        return user;

    }

    private void sync(UserDetails user) {
        new RetrofitCalls().synUserPreference(user, token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    Log.inform("Successfully synced user data!");
                } else {
                    Log.error(response.message());
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.error(t.getLocalizedMessage());
            }
        });
    }

    private void deleteOld() {
        new RetrofitCalls().deleteOld(prefs.getUserEmail(), token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.inform("User details are deleted from old database");
                    deleteAuth();
                } else {
                    Log.error(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.error(t.getLocalizedMessage());
            }
        });

    }

    private void deleteAuth() {
        new RetrofitCalls().deleteAuth(uid, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.inform("User details are deleted from EmailAuth");
                    updateMigration();
                } else {
                    Log.error(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.error(t.getLocalizedMessage());
            }
        });
    }

    private void updateMigration() {
        new RetrofitCalls().updateMigration(uid, token).enqueue(new Callback<UpdateMigrationID>() {
            @Override
            public void onResponse(Call<UpdateMigrationID> call, Response<UpdateMigrationID> response) {
                if (response.isSuccessful()) {
                    Log.inform("User details are deleted from EmailAuth");
                    prefs.setMigrationID(CURRENT_MIGRATION_ID);
                } else {
                    Log.error(response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateMigrationID> call, Throwable t) {
                Log.error(t.getLocalizedMessage());
            }
        });
    }
}
