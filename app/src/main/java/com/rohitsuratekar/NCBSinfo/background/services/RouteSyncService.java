package com.rohitsuratekar.NCBSinfo.background.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.rohitsuratekar.NCBSinfo.background.networking.CounterDetails;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteSyncService extends IntentService {

    public final static String SYNC_ALL = "synRoutes"; // Sync all current database to server
    public final static String RETRIEVE_ALL = "retrieveAll"; // Save all database from server
    public final static String TEST = "test";

    public RouteSyncService(String name) {
        super(name);
    }

    public RouteSyncService() {
        super("RouteSyncService");
    }

    private FirebaseAuth mAuth;
    private String uid;
    private String token;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.inform("Route syncing started");
        mAuth = FirebaseAuth.getInstance();
        final String action = intent.getAction();
        if (mAuth.getCurrentUser() != null && action != null) {

            mAuth.getCurrentUser().getToken(false).addOnCompleteListener(
                    new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                uid = mAuth.getCurrentUser().getUid();
                                token = task.getResult().getToken();
                                switch (action) {
                                    case SYNC_ALL:
                                        synAll();
                                        break;
                                    case RETRIEVE_ALL:
                                        //TODO
                                        break;
                                    case TEST:
                                        test();
                                        break;
                                }
                            } else {
                                Log.error(task.getException().getLocalizedMessage());
                            }
                        }
                    }
            );
        }

    }


    private void synAll() {
        //Get timestamp
        String syncTime = General.timeStamp();
        //Update Shared preferences
        new AppPrefs(getBaseContext()).setLastSync(syncTime);
        //Update Database entries
        new RouteData(getBaseContext()).updateSyncTime(syncTime);
        //Update all entries on server
        new RetrofitCalls().syncRoutes(uid, new RouteData(getBaseContext()).getAll(), token)
                .enqueue(new Callback<HashMap<String, RouteModel>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, RouteModel>> call, Response<HashMap<String, RouteModel>> response) {
                        if (response.isSuccessful()) {
                            Log.inform("Successfully synced all route information");
                        } else {
                            Log.error(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, RouteModel>> call, Throwable t) {
                        Log.error(t.getLocalizedMessage());
                    }
                });
    }

    private void test() {
        CounterDetails d = new CounterDetails("ashsh", 1);
        new RetrofitCalls().updateCounter(d, token)
                .enqueue(new Callback<CounterDetails>() {
                    @Override
                    public void onResponse(Call<CounterDetails> call, Response<CounterDetails> response) {
                        Log.inform(response.message());
                    }

                    @Override
                    public void onFailure(Call<CounterDetails> call, Throwable t) {
                        Log.inform(t.getLocalizedMessage());
                    }
                });
    }
}
