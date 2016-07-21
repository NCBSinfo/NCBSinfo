package com.rohitsuratekar.NCBSinfo.background;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class FireBaseID extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    Preferences pref;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        pref = new Preferences(getBaseContext());
        pref.user().setToken(refreshedToken);
    }
    // [END refresh_token]
}
