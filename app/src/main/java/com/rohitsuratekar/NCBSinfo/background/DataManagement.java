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
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;

import java.util.Map;

/**
 * This service is to request network calls in background.
 * Use this to sync data with FIrebase Database.
 * For regular network calls , use NetwrokOperations service.
 * This can be triggered by alarm manager
 */

public class DataManagement extends IntentService implements UserInformation {

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
            Log.i(TAG, pref.getString(USER_TYPE, currentUser.NEW_USER));
            if (!pref.getString(USER_TYPE, currentUser.NEW_USER).equals(currentUser.OLD_USER)) {
                mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.USERNAME).setValue(pref.getString(registration.USERNAME, "Username"));
                mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.EMAIL).setValue(pref.getString(registration.EMAIL, "email@domain.com"));
                mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.TOKEN).setValue(pref.getString(registration.FIREBASE_TOKEN, "null"));
                mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.DEFAULT_ROUTE).setValue(pref.getString(preferences.DEFAULT_ROUTE, "0"));
                mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.RESEARCH_TALK).setValue(pref.getInt(registration.RESEARCH_TALK, 1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            pref.edit().putString(USER_TYPE, currentUser.REGULAR_USER).apply();
                        }
                    }
                });
                final String fieldEMail = user.getEmail().replace("@", "_").replace(".", "_");
                //This boolen is set by remote config and will be switched off after camp is done
                if (pref.getBoolean(registration.camp16.CAMP_ACCESS, false)) {
                    Log.i(TAG, "Fetching CAMP database");
                    mDatabase.child(RemoteConstants.CAMP_NODE).child(fieldEMail).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (child.getKey().equals(fieldEMail)) {
                                            //TODO: for types of users
                                            Log.i("Key value", child.getValue().toString());
                                        }
                                    }
                                    pref.edit().putBoolean(registration.camp16.IS_CAMP_USER, true).apply();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.i(TAG, databaseError.toException().getMessage());
                                    if (databaseError.toException().getMessage().contains("Permission denied")) {
                                        pref.edit().putBoolean(registration.camp16.IS_CAMP_USER, false).apply();
                                    }

                                }
                            });
                }
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
            mDatabase.child(RemoteConstants.USER_NODE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data.get(RemoteConstants.USERNAME) != null) {
                        pref.edit().putString(registration.USERNAME, data.get(RemoteConstants.USERNAME).toString()).apply();
                    }
                    if (data.get(RemoteConstants.EMAIL) != null) {
                        pref.edit().putString(registration.EMAIL, data.get(RemoteConstants.EMAIL).toString()).apply();
                    }
                    if (data.get(RemoteConstants.RESEARCH_TALK) != null) {
                        pref.edit().putInt(registration.RESEARCH_TALK, Integer.parseInt(data.get(RemoteConstants.RESEARCH_TALK).toString())).apply();
                    }
                    if (data.get(RemoteConstants.DEFAULT_ROUTE) != null) {
                        pref.edit().putString(preferences.DEFAULT_ROUTE, data.get(RemoteConstants.DEFAULT_ROUTE).toString()).apply();
                    }
                    pref.edit().putString(USER_TYPE, currentUser.REGULAR_USER).apply();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });


        }

    }
}
