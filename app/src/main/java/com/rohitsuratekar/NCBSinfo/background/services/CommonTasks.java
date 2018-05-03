package com.rohitsuratekar.NCBSinfo.background.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rohitsuratekar.NCBSinfo.background.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.background.networking.PublicInfo;
import com.rohitsuratekar.NCBSinfo.background.networking.RetrofitCalls;
import com.rohitsuratekar.NCBSinfo.background.networking.RouteModel;
import com.rohitsuratekar.NCBSinfo.background.networking.UserDetails;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit Suratekar on 13-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class CommonTasks extends IntentService {

    private static final String SEND_FAVORITE_CHANGE = "com.rohitsuratekar.NCBSinfo.background.action.sendfav";
    private static final String SYNC_USER_DETAILS = "com.rohitsuratekar.NCBSinfo.background.action.syncUser";
    private static final String SYNC_ROUTES = "com.rohitsuratekar.NCBSinfo.background.action.syncRoutes";
    private static final String SYNC_PUBLIC_DATA = "com.rohitsuratekar.NCBSinfo.background.action.syncPublicData";
    private static final String RESET_ROUTES = "com.rohitsuratekar.NCBSinfo.background.action.resetRoutes";
    private static final String DELETE_ROUTES = "com.rohitsuratekar.NCBSinfo.background.action.deleteRoutes";
    private static final String DELETE_TRIPS = "com.rohitsuratekar.NCBSinfo.background.action.deleteTrips";

    private static final String FAV_ROUTE = "FavRoute";
    private static final String DEL_ORIGIN = "delOrigin";
    private static final String DEL_DESTINATION = "delDestination";
    private static final String DEL_TYPE = "delType";
    private static final String DEL_DAY = "delDay";

    public CommonTasks() {
        super("CommonTasks");
    }

    public static void sendFavoriteRoute(Context context, int routeID) {
        Log.inform("New favorite route");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SEND_FAVORITE_CHANGE);
        intent.putExtra(FAV_ROUTE, routeID);
        context.startService(intent);
    }

    public static void deleteRoute(Context context, String origin, String destination, String type) {
        Log.inform("Deleting route " + origin + destination + type);
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(DELETE_ROUTES);
        intent.putExtra(DEL_ORIGIN, origin);
        intent.putExtra(DEL_DESTINATION, destination);
        intent.putExtra(DEL_TYPE, type);
        context.startService(intent);
    }

    public static void syncUserDetails(Context context) {
        Log.inform("User Sync service started.");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SYNC_USER_DETAILS);
        context.startService(intent);
    }

    public static void syncRoutes(Context context) {
        Log.inform("Route Sync service started.");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SYNC_ROUTES);
        context.startService(intent);
    }

    public static void syncPublicData(Context context) {
        //TODO  : Some new mechanism to update routes
        Log.inform("Public data sync service started.");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SYNC_PUBLIC_DATA);
        context.startService(intent);
    }

    public static void deleteSpecificTrips(Context context, String origin, String destination, String type, int day) {
        Log.inform("Deleting specific trips");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(DELETE_TRIPS);
        intent.putExtra(DEL_ORIGIN, origin);
        intent.putExtra(DEL_DESTINATION, destination);
        intent.putExtra(DEL_TYPE, type);
        intent.putExtra(DEL_DAY, day);
        context.startService(intent);
    }

    public static void resetAllRoutes(Context context) {
        Log.inform("Resetting all routes");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(RESET_ROUTES);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SEND_FAVORITE_CHANGE.equals(action)) {
                changeFavRoute(intent.getIntExtra(FAV_ROUTE, -1));
            } else if (SYNC_USER_DETAILS.equals(action)) {
                syncUser();
            } else if (SYNC_ROUTES.equals(action)) {
                syncRoutes();
            } else if (RESET_ROUTES.equals(action)) {
                resetRoutes();
            } else if (SYNC_PUBLIC_DATA.equals(action)) {
                syncPublic();
            } else if (DELETE_ROUTES.equals(action)) {
                String o = intent.getStringExtra(DEL_ORIGIN);
                String d = intent.getStringExtra(DEL_DESTINATION);
                String t = intent.getStringExtra(DEL_TYPE);
                deleteRoute(o, d, t);
            } else if (DELETE_TRIPS.equals(action)) {
                String o = intent.getStringExtra(DEL_ORIGIN);
                String d = intent.getStringExtra(DEL_DESTINATION);
                String t = intent.getStringExtra(DEL_TYPE);
                int day = intent.getIntExtra(DEL_DAY, 0);
                deleteTrips(o, d, t, day);
            } else {
                reportError(action + " not found.");
            }
        }
    }

    private void syncPublic() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        AppPrefs prefs = new AppPrefs(getApplicationContext());
        //Only do this for logged in users
        if (prefs.isUsedLoggedIn()) {
            if (auth.getCurrentUser() != null) {
                auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            getPublicInfo(task.getResult().getToken());
                        } else {
                            reportError(task.getException().getLocalizedMessage());
                        }
                    }
                });
            } else {
                reportError("Unable to Sync User details");
            }
        } else {
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && auth.getCurrentUser() != null) {
                        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    getPublicInfo(task.getResult().getToken());
                                } else {
                                    reportError(task.getException().getLocalizedMessage());
                                }

                            }
                        });
                    } else {
                        reportError("Unable to authenticate anonymously");
                    }
                }
            });
        }
    }

    private void getPublicInfo(String token) {
        new RetrofitCalls().getPublicInfo(token).enqueue(new Callback<PublicInfo>() {
            @Override
            public void onResponse(@NonNull Call<PublicInfo> call, @NonNull Response<PublicInfo> response) {
                if (response.isSuccessful()) {
                    PublicInfo info = response.body();
                    if (info != null) {
                        //todo
                    } else {
                        Log.inform("No public information found");
                    }
                } else {
                    Log.error(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PublicInfo> call, @NonNull Throwable t) {
                Log.error(t.getLocalizedMessage());
            }
        });
    }


    private void deleteRoute(String origin, String destination, String type) {
        if (origin != null && destination != null && type != null) {
            AppData db = AppData.getDatabase(getApplicationContext());
            int routeNo = db.routes().getRouteNo(origin, destination, type);
            if (routeNo != 0) {
                db.routes().deleteRouteByNumber(routeNo);
                db.trips().deleteTripsByRoute(routeNo);
            } else {
                reportError("No such route found " + origin + "-" + destination + " " + type);
            }
        } else {
            reportError("Unable to delete route " + origin + "-" + destination + " " + type);
        }
        syncRoutes();
    }


    private void changeFavRoute(int routeID) {
        if (routeID != -1) {
            AppData db = AppData.getDatabase(getApplicationContext());
            db.routes().removeAllFavorite();
            db.routes().setFavorite(routeID);
        } else {
            reportError("Unable to set favorite route");
        }
    }

    private void syncUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AppPrefs prefs = new AppPrefs(getApplicationContext());
        //Only do this for logged in users
        if (prefs.isUsedLoggedIn()) {
            if (auth.getCurrentUser() != null) {
                final UserDetails details = getUserDetails(getApplicationContext(), auth);
                auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            new RetrofitCalls().synUserPreference(details, task.getResult().getToken()).enqueue(new Callback<UserDetails>() {
                                @Override
                                public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                                    Log.inform("User details Synced: " + details.getUid());
                                }

                                @Override
                                public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                                    reportError(t.getLocalizedMessage());
                                }
                            });

                        } else {
                            if (task.getException() != null) {
                                reportError(task.getException().getLocalizedMessage());
                            } else {
                                reportError("Unable to get access token.");
                            }
                        }
                    }
                });
            } else {
                reportError("Unable to Sync User details");
            }
        }
    }


    private UserDetails getUserDetails(Context context, FirebaseAuth auth) {

        AppPrefs prefs = new AppPrefs(context);

        String syncTime = General.timeStamp();
        //Update Shared preferences
        prefs.setLastSync(syncTime);
        UserDetails user = new UserDetails();
        user.setName(prefs.getUserName());
        user.setEmail(prefs.getUserEmail());
        if (auth.getCurrentUser() != null) {
            user.setUid(auth.getCurrentUser().getUid());
        } else {
            reportError("Unable to user ID while syncing");
        }
        user.setFcmToken(FirebaseInstanceId.getInstance().getToken());
        user.setLastSync(syncTime);
        user.setFavoriteType(prefs.getFavoriteType().toLowerCase());
        user.setFavoriteOrigin(prefs.getFavoriteOrigin().toLowerCase());
        user.setFavoriteDestination(prefs.getFavoriteDestination().toLowerCase());
        user.setMigrationID(prefs.getMigrationId());
        if (prefs.areNotificationsAllowed()) {
            user.setNotifications("ON");
        } else {
            user.setNotifications("OFF");
        }
        return user;


    }

    private void syncRoutes() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        AppPrefs prefs = new AppPrefs(getApplicationContext());
        //Only do this for logged in users
        if (prefs.isUsedLoggedIn()) {
            if (auth.getCurrentUser() != null) {
                final List<RouteModel> modelList = createRouteModels();
                auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            new RetrofitCalls().syncRoutes(auth.getCurrentUser().getUid(), modelList, task.getResult().getToken()).enqueue(new Callback<HashMap<String, RouteModel>>() {
                                @Override
                                public void onResponse(@NonNull Call<HashMap<String, RouteModel>> call, @NonNull Response<HashMap<String, RouteModel>> response) {
                                    Log.inform("Route details synced successfully");
                                }

                                @Override
                                public void onFailure(@NonNull Call<HashMap<String, RouteModel>> call, @NonNull Throwable t) {
                                    reportError(t.getLocalizedMessage());
                                }
                            });

                        } else {
                            if (task.getException() != null) {
                                reportError(task.getException().getLocalizedMessage());
                            } else {
                                reportError("Unable to get access token.");
                            }
                        }
                    }
                });
            } else {
                reportError("Unable to Sync Route details");
            }
        }
    }


    private List<RouteModel> createRouteModels() {
        String syncTime = General.timeStamp();
        AppData db = AppData.getDatabase(getApplicationContext());
        List<RouteData> routeData = db.routes().getRouteNames();
        List<RouteModel> modelList = new ArrayList<>();
        db.routes().updateSync(syncTime);
        for (RouteData r : routeData) {
            List<TripData> tripData = db.trips().getTripsByRoute(r.getRouteID());
            for (TripData t : tripData) {
                RouteModel model = new RouteModel();
                model.setOrigin(r.getOrigin());
                model.setDestination(r.getDestination());
                model.setType(r.getType());
                model.setCreatedOn(r.getCreatedOn());
                model.setModifiedOn(r.getModifiedOn());
                model.setSyncedOn(syncTime);
                model.setAuthor(r.getAuthor());
                model.setDay(t.getDay());
                model.setTrips(t.getTrips());
                modelList.add(model);
            }
        }
        return modelList;
    }

    private void resetRoutes() {
        AppData db = AppData.getDatabase(getApplicationContext());
        db.routes().deleteAll();
        db.trips().deleteAll();
        Log.inform("All Routes deleted");
        new CreateDefaultRoutes(getApplicationContext(), new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {

            }
        }).execute();
    }

    private void deleteTrips(String origin, String destination, String type, int day) {
        if (origin != null && destination != null && type != null) {
            AppData db = AppData.getDatabase(getApplicationContext());
            int routeNo = db.routes().getRouteNo(origin, destination, type);
            if (routeNo != 0) {
                Log.inform("Deleting trips for the day: " + day);
                int tripID = db.trips().getTripID(routeNo, day);
                db.trips().deleteTripsByTrip(tripID);

                List<TripData> leftData = db.trips().getTripsByRoute(routeNo);

                if (leftData.size() == 0) {
                    Log.inform("No Trips are left hence deleting route as well.");
                    db.routes().deleteRouteByNumber(routeNo);
                }

            } else {
                reportError("No such route found " + origin + "-" + destination + " " + type);
            }
        } else {
            reportError("Unable to delete route " + origin + "-" + destination + " " + type);
        }
        syncRoutes();
    }


    private void reportError(String message) {
        Log.error(message);
        if (!message.toLowerCase().contains("connect timed out")) {
            if (!message.toLowerCase().contains("connection abort")) {
                Crashlytics.logException(new Exception(message));
            }
        }
    }

}