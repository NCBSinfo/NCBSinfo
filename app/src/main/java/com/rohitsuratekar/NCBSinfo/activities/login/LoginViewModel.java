package com.rohitsuratekar.NCBSinfo.activities.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.rohitsuratekar.NCBSinfo.background.CommonTasks;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.networking.RouteModel;
import com.rohitsuratekar.NCBSinfo.background.networking.UpdateMigrationID;
import com.rohitsuratekar.NCBSinfo.background.networking.UserDetails;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

// Keep Public Class
public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> userDataReceived = new MutableLiveData<>();
    private MutableLiveData<String> errorOccured = new MutableLiveData<>();

    public void startLoadingProcess(final Context context) {
        AppPrefs prefs = new AppPrefs(context);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String user = auth.getCurrentUser().getUid();
                        String email = auth.getCurrentUser().getEmail();
                        checkIfOld(task.getResult().getToken(), user, context, email);
                    } else {
                        if (task.getException() != null) {
                            showError(task.getException().getLocalizedMessage());
                        } else {
                            showError("Unable to get user data! Please try again.");
                        }
                    }
                }
            });

        } else {
            showError("Something is wrong, please try logging again.");
        }
    }

    private void showError(String message) {
        Log.error(message);
        errorOccured.postValue(message);
    }

    public MutableLiveData<String> getErrorOccured() {
        return errorOccured;
    }

    public MutableLiveData<Boolean> getUserDataReceived() {
        return userDataReceived;
    }

    /***
     * Checks if current user is old user and have data in database.
     * @param token : Authorization Token
     * @param uid : UID
     * @param context : Context
     */
    private void checkIfOld(final String token, final String uid, final Context context, final String email) {
        new RetrofitCalls().getUserDetails(uid, token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                UserDetails details = response.body();
                if (details != null) {
                    if (details.getMigrationID().equals("v6")) {
                        Log.inform("User details are up to date, starting data loading");
                        loadData(details, context);
                    } else {
                        Log.inform("Old user details found. Migrating database");
                        CommonTasks.syncRoutes(context);
                        deleteOld(uid, token, email);
                    }

                } else {
                    Log.inform("No user details found. Sending current details.");
                    CommonTasks.syncUserDetails(context);
                    CommonTasks.syncRoutes(context);
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

    private void deleteOld(final String uid, final String token, String email) {
        new RetrofitCalls().deleteOld(email, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                deleteAuth(uid, token);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

    private void deleteAuth(final String uid, final String token) {
        new RetrofitCalls().deleteAuth(uid, token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.inform("All old records deleted");
                migrateNumber(uid, token);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

    private void migrateNumber(String uid, String token) {
        new RetrofitCalls().updateMigration(uid, token).enqueue(new Callback<UpdateMigrationID>() {
            @Override
            public void onResponse(@NonNull Call<UpdateMigrationID> call, @NonNull Response<UpdateMigrationID> response) {
                Log.inform("Migration Number Updated");
            }

            @Override
            public void onFailure(@NonNull Call<UpdateMigrationID> call, @NonNull Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

    private void loadData(UserDetails details, Context context) {
        Gson gson = new Gson();
        final AppPrefs prefs = new AppPrefs(context);
        prefs.setFavoriteOrigin(details.getFavoriteOrigin());
        prefs.setUserEmail(details.getName());
        prefs.setUserEmail(details.getEmail());
        prefs.setLastSync(General.timeStamp());
        prefs.setFavoriteType(details.getFavoriteType().toLowerCase());
        List<RouteModel> modelList = new ArrayList<>();
        if (details.getRoutes() != null) {
            if (details.getRoutes().size() > 0) {
                for (Object obj : details.getRoutes().values()) {
                    RouteModel model = gson.fromJson(gson.toJson(obj), RouteModel.class);
                    modelList.add(model);
                }
            }
        }

        String[] fav = new String[]{details.getFavoriteOrigin(), details.getFavoriteDestination(), details.getFavoriteType()};

        new MakeRoutes(context, fav, modelList, new OnFinish() {
            @Override
            public void finished() {
                Log.inform("All Routes Synced");
                prefs.userLoggedIn();
                userDataReceived.postValue(true);
            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {

            }
        }).execute();
    }

    private static class MakeRoutes extends AsyncTask<Void, Void, Void> {
        private OnFinish onFinish;
        private AppData db;
        private List<RouteModel> modelList;
        private String[] fav;

        MakeRoutes(Context context, String[] fav, List<RouteModel> modelList, OnFinish onFinish) {
            this.onFinish = onFinish;
            this.fav = fav;
            this.modelList = modelList;
            this.db = AppData.getDatabase(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Delete all old routes
            db.routes().deletAll();
            db.trips().deletAll();
            Log.inform("All Old routes deleted.");

            for (RouteModel model : modelList) {
                String syncTime = General.timeStamp();
                RouteData data;
                String favString = "no";
                if (fav[0].equals(model.getOrigin()) && fav[1].equals(model.getDestination()) && fav[2].equals(model.getType().toLowerCase())) {
                    favString = "yes";
                }

                data = new RouteData();
                data.setOrigin(model.getOrigin());
                data.setDestination(model.getDestination());
                data.setType(model.getType().toLowerCase());
                data.setCreatedOn(model.getCreatedOn());
                data.setModifiedOn(model.getModifiedOn());
                data.setAuthor(model.getAuthor());
                data.setSynced(syncTime);
                data.setFavorite(favString);

                long routeID = db.routes().getRouteNo(data.getOrigin(), data.getDestination(), data.getType());

                if (routeID == 0) {
                    routeID = db.routes().insertRoute(data);
                    Log.inform("Route created : " + data.getOrigin() + "-" + data.getDestination() + " " + data.getType());
                }

                TripData trip = new TripData();
                trip.setRouteID((int) routeID);
                trip.setDay(model.getDay());
                trip.setTrips(model.getTrips());
                db.trips().insertTrips(trip);
            }

            onFinish.finished();

            return null;
        }
    }

}
