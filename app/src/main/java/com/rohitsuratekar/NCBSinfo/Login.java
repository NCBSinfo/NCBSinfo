package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rohitsuratekar.NCBSinfo.common.Utilities;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;
import com.rohitsuratekar.NCBSinfo.online.fragments.RegisterFragment;

public class Login extends AppCompatActivity {

    //Public constants
    public static final String IS_OLD_VERSION = "isOldVersion";

    //Local constants
    private final String TAG = this.getClass().getSimpleName();

    //Local variables
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Set up remote configuration
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //TODO: Change debug mode while production
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);

        //Add listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (pref.getBoolean(RegisterFragment.REGISTERED, false)) {
                        mDatabase.child(RemoteConstants.USER_NODE+ "/" + user.getUid() + "/"+ RemoteConstants.USERNAME).setValue(pref.getString(RegisterFragment.USERNAME, "Username"));
                        mDatabase.child(RemoteConstants.USER_NODE+ "/" + user.getUid() + "/"+ RemoteConstants.EMAIL).setValue(pref.getString(RegisterFragment.EMAIL, "email@domain.com"));
                        mDatabase.child(RemoteConstants.USER_NODE+ "/" + user.getUid() + "/"+ RemoteConstants.RESEARCH_TALK).setValue(pref.getInt(RegisterFragment.RESEARCH_TALK, 1));
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }

            }
        };
        //Get remote configuration
        getConfiguration();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_fragment, new RegisterFragment())
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this, Home.class));
    }

    private void getConfiguration() {
        long cacheExpiration = RemoteConstants.CACHE_EXPIRATION;

        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig != null) {
            if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                cacheExpiration = 0;
            }
            if (new Utilities().isOnline(getBaseContext())) {
                mFirebaseRemoteConfig.fetch(cacheExpiration)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Fetch Succeeded");
                                    // Once the config is successfully fetched it must be activated before newly fetched
                                    // values are returned.
                                    mFirebaseRemoteConfig.activateFetched();
                                    setTransportValue();
                                    pref.edit().putBoolean(IS_OLD_VERSION, mFirebaseRemoteConfig.getBoolean(IS_OLD_VERSION)).apply();
                                } else {
                                    Log.d(TAG, "Fetch failed");
                                }
                            }
                        });
            } else {
                Log.e(TAG, "No connection detected!");
            }
        }


    }


    public void setTransportValue() {

        pref.edit().putString(TransportConstants.NCBS_IISC_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_IISC_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_IISC_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_IISC_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.IISC_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.IISC_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_MANDARA_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_MANDARA_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.MANDARA_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.MANDARA_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_ICTS_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_ICTS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.ICTS_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.ICTS_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_CBL, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_CBL)).apply();
        pref.edit().putString(TransportConstants.BUGGY_NCBS, mFirebaseRemoteConfig.getString(TransportConstants.BUGGY_NCBS)).apply();
        pref.edit().putString(TransportConstants.BUGGY_MANDARA, mFirebaseRemoteConfig.getString(TransportConstants.BUGGY_MANDARA)).apply();

    }


}
