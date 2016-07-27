package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.background.firebase.DataBuilder;
import com.rohitsuratekar.NCBSinfo.background.firebase.FireBaseConstants;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.Map;

/**
 * This service is to request network calls in background.
 * Use this to sync data with Firebase Database.
 * For regular network calls , use NetwrokOperations service.
 * This can be triggered by alarm manager
 */

public class DataManagement extends IntentService implements AppConstants, FireBaseConstants {

    //Public Constants
    public static final String INTENT = DataManagement.class.getName();
    public static final String SEND_FIREBASEDATA = "send_firebaseData";
    public static final String FETCH_FIREBASE_DATA = "fetch_firebaseData";

    //Local constants
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private Preferences pref;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public DataManagement(String name) {
        super(name);
    }

    public DataManagement() {
        super(DataManagement.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Data Service started at " + General.timeStamp());
        this.context = getBaseContext();
        this.pref = new Preferences(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        //Do not waste network call if user is on proxy
        if (!new General().isOnProxy()) {

            String trigger = intent.getStringExtra(INTENT);
            //Strict policy for offline mode
            if (trigger != null
                    && !pref.app().getMode().equals(modes.UNKNOWN)
                    && !pref.app().getMode().equals(modes.OFFLINE)) {

                switch (trigger) {
                    case SEND_FIREBASEDATA:
                        if (!pref.network().isOldDeleted()) {
                            migrateToNew();
                        } else {
                            send();
                        }
                        break;
                    case FETCH_FIREBASE_DATA:
                        readData();
                        break;
                }
            } else {
                Log.e(TAG, "User is on offline mode or wrong trigger sent");
            }
        } else {
            Log.e(TAG, "User is on proxy, database operations are suspended");
        }

    }


    @SuppressWarnings("unchecked")
    private void migrateToNew() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (!pref.network().isOldDeleted()) {
                mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                        if (data != null) {
                            if (data.get("username") != null) {
                                pref.user().setName(data.get("username").toString());
                            }
                            if (data.get("defaultRoute") != null) {
                                pref.user().setDefaultRoute(new TransportHelper().getRoute(Integer.parseInt(data.get("defaultRoute").toString())));
                            }
                            sendFirstTimeDetails();
                            //Deleting from Old Database
                            mDatabase.child("users").child(user.getUid()).removeValue();
                            pref.network().setOldDeleted();

                        } else {
                            Log.e(TAG, "User has no records");
                            //Don't request this query again
                            pref.network().setOldDeleted();
                            sendFirstTimeDetails();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                        Log.e(TAG, "User has no previous data on our server");
                        //Database error will be created if user is not registered with our database before.
                        // Hence directly send data to new database node
                        pref.network().setOldDeleted();
                        sendFirstTimeDetails();
                    }
                });
            }//Old Deleted
        }
    }

    private void sendFirstTimeDetails() {
        if (mAuth.getCurrentUser() != null) {
            pref.user().setToken(FirebaseInstanceId.getInstance().getToken());
            pref.user().setFirebaseID(mAuth.getCurrentUser().getUid());
            mDatabase.child(AUTH_EMAIL).child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        send();
                        //Convert old user to regular for sending data to server
                        pref.user().setUserType(userType.REGULAR_USER);
                        Log.i(TAG, "Data Migrated to new node and authentication email stored.");
                    } else {
                        Log.e(TAG, task.getException().getLocalizedMessage());
                    }
                }
            });
        }
    }

    private void send() {
        if (mAuth.getCurrentUser() != null) {
            if (!pref.user().getUserType().equals(userType.OLD_USER)) {
                DataBuilder dataBuilder = new DataBuilder(context);
                mDatabase.child(USER_NODE).child(makePath(mAuth.getCurrentUser().getEmail())).setValue(dataBuilder.make()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pref.user().setUserType(userType.REGULAR_USER);
                            pref.network().setLastFirebaseSync(General.timeStamp());
                            Log.i(TAG, "Data sent to server");
                        } else {
                            Log.e(TAG, task.getException().getLocalizedMessage());
                        }
                    }
                });
            } //Old user
            else {
                Log.i(TAG, "Trying to retrieve old user data");
                readData();
            }
        }
    }

    public String makePath(String email) {
        return email.replace("@", "_").replace(".", "_").trim();
    }


    @SuppressWarnings("unchecked")
    private void readData() {

        if (mAuth.getCurrentUser() != null) {
            mDatabase.child(USER_NODE).child(makePath(mAuth.getCurrentUser().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data != null) {
                        if (data.get(NAME) != null) {
                            pref.user().setName(data.get(NAME).toString());
                        }
                        if (data.get(DEFAULT_ROUTE) != null) {
                            pref.user().setDefaultRoute(new TransportHelper().getRoute(Integer.valueOf(data.get(DEFAULT_ROUTE).toString())));
                        }
                        if (data.get(NOTIFICATION_PREFERENCE) != null) {
                            switch (data.get(NOTIFICATION_PREFERENCE).toString()) {
                                case "0":
                                    pref.user().notificationAllowed(true);
                                    break; //Default
                                case "1":
                                    pref.user().notificationAllowed(true);
                                    break; //Preference
                                case "2":
                                    pref.user().notificationAllowed(false);
                                    break; //Preference
                            }
                        }
                        //Set user to regular after reading first time
                        pref.user().setUserType(userType.REGULAR_USER);
                        Log.i(TAG, "Data retrieved successfully.");
                    } else {
                        Log.e(TAG, "No such user data found, sending new information");
                        sendFirstTimeDetails();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                    sendFirstTimeDetails();
                }
            });


        }
        {
            Log.e(TAG, "No user found");
        }
    }


}
