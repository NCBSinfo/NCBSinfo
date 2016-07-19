package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * This service is to request network calls in background.
 * Use this to sync data with FIrebase Database.
 * For regular network calls , use NetwrokOperations service.
 * This can be triggered by alarm manager
 */

public class DataManagement extends IntentService implements AppConstants {

    //Public Constants
    public static final String INTENT = DataManagement.class.getName();
    public static final String SEND_FIREBASEDATE = "send_firebaseData";
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
        Log.d(TAG, "Data Service started");
        this.context = getBaseContext();
        this.pref = new Preferences(context);
        //Initialize Firebase
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        String trigger = intent.getStringExtra(INTENT);
        switch (trigger) {
            case SEND_FIREBASEDATE:
                sendDetails();
                break;
            case FETCH_FIREBASE_DATA:
                //TODO
                break;
        }

    }

    private void sendDetails() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i(TAG, pref.user().getUserType().getValue());
//            if (!pref.user().getUserType().equals(userType.OLD_USER)) {
//                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.USERNAME).setValue(pref.user().getName());
//                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.EMAIL).setValue(pref.user().getEmail());
//                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.TOKEN).setValue(pref.user().getToken());
//                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.LATEST_APP).setValue(pref.app().getAppVesion());
//                mDatabase.child(nodes.USER_NODE + "/" + user.getUid() + "/" + data.DEFAULT_ROUTE).setValue(pref.user().getDefaultRoute()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isComplete()) {
//                            pref.user().setUserType(userType.REGULAR_USER);
//                        }
//                    }
//                });
            //          }//Is not old user
            //          else {
            //TODO
            //          }
        }


    }

}
