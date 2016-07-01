package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.rohitsuratekar.NCBSinfo.interfaces.RemoteData;
import com.rohitsuratekar.NCBSinfo.interfaces.User;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentUser;

import java.util.Map;

/**
 * This service is to request network calls in background.
 * Use this to sync data with FIrebase Database.
 * For regular network calls , use NetwrokOperations service.
 * This can be triggered by alarm manager
 */

public class DataManagement extends IntentService implements User, RemoteData {

    //Public Constants
    public static final String INTENT = "dataIntent";
    public static final String SEND_FIREBASEDATE = "send_firebaseData";
    public static final String FETCH_FIREBASE_DATA = "fetch_firebaseData";

    //Local constants
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private SharedPreferences pref;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CurrentUser userData;

    public DataManagement(String name) {
        super(name);
    }

    public DataManagement() {
        super(DataManagement.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Data Service started");
        this.context = getBaseContext();
        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.userData = new CurrentUser(context);
        //Initialize Firebase
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        String trigger = intent.getStringExtra(INTENT);
        switch (trigger) {
            case SEND_FIREBASEDATE:
                sendDetails();
                break;
            case FETCH_FIREBASE_DATA:
                fetchFirebaseData();
                break;
        }

    }

    private void sendDetails() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i(TAG, pref.getString(USER_TYPE, currentUserType.NEW_USER));
            if (!pref.getString(USER_TYPE, currentUserType.NEW_USER).equals(currentUserType.OLD_USER)) {
                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.USERNAME).setValue(userData.getName());
                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.EMAIL).setValue(userData.getEmail());
                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.TOKEN).setValue(userData.getToken());
                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.DEFAULT_ROUTE).setValue(userData.getDefaultRoute()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            pref.edit().putString(USER_TYPE, currentUserType.REGULAR_USER).apply();
                        }
                    }
                });
            }//Is not old user
            else {
                fetchFirebaseData();
            }
        }


    }

    @SuppressWarnings("unchecked")
    private void fetchFirebaseData() {
        Log.i(TAG, "Fetching old data from server");
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabase.child(nodes.USER_NODE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data != null) {
                        if (data.get(RemoteData.data.USERNAME) != null) {
                            pref.edit().putString(profile.NAME, data.get(RemoteData.data.USERNAME).toString()).apply();
                        }
                        if (data.get(RemoteData.data.EMAIL) != null) {
                            pref.edit().putString(profile.EMAIL, data.get(RemoteData.data.EMAIL).toString()).apply();
                        }
                        if (data.get(RemoteData.data.DEFAULT_ROUTE) != null) {
                            pref.edit().putInt(preferences.DEFAULT_ROUTE, Integer.parseInt(data.get(RemoteData.data.DEFAULT_ROUTE).toString())).apply();
                        }
                        pref.edit().putString(USER_TYPE, currentUserType.REGULAR_USER).apply();
                    } else {
                        //If user has not registered with their data last time, send now.
                        //However this will not sync their previous data
                        //TODO: implement something here when Firebase will start working with proxy
                        pref.edit().putString(USER_TYPE, currentUserType.NEW_USER).apply();
                        sendDetails();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });


        }

    }
}
