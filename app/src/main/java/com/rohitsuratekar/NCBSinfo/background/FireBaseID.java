package com.rohitsuratekar.NCBSinfo.background;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;

public class FireBaseID extends FirebaseInstanceIdService implements NetworkConstants, UserInformation {

    public static String REGULAR_USER = "normal";

    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences pref;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        pref.edit().putString(registration.FIREBASE_TOKEN, refreshedToken).apply();
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //Subscribe ID to public topic. All devices will be part of public group
        if (pref.getBoolean(registration.REGISTERED, false)) {
            pref.edit().putString(registration.FIREBASE_TOKEN, token).apply();
            pref.edit().putString(registration.USER_TYPE, REGULAR_USER).apply();
            FirebaseMessaging.getInstance().subscribeToTopic(topics.PUBLIC);
            FirebaseMessaging.getInstance().subscribeToTopic(topics.EMERGENCY);
            Intent service = new Intent(getBaseContext(), NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.REGISTER);
            getBaseContext().startService(service);
            Log.d(TAG, "Subscribed with topic");
        }
    }
}