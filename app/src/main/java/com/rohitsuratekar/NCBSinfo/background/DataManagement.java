package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;

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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String trigger = intent.getStringExtra(INTENT);
        switch (trigger) {
            case SEND_FIREBASEDATE:
                sendDetails();
                break;
        }

    }

    private void sendDetails() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.USERNAME).setValue(pref.getString(registration.USERNAME, "Username"));
            mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.EMAIL).setValue(pref.getString(registration.EMAIL, "email@domain.com"));
            mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.TOKEN).setValue(pref.getString(registration.FIREBASE_TOKEN, "null"));
            mDatabase.child(RemoteConstants.USER_NODE + "/" + user.getUid() + "/" + RemoteConstants.RESEARCH_TALK).setValue(pref.getInt(registration.RESEARCH_TALK, 1));
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
        }


    }
}
