package com.rohitsuratekar.NCBSinfo.background;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseMessage extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().get("rcode")!=null) {

            //Special action by with "rcode" field
            switch (remoteMessage.getData().get("rcode")) {
                case "time":
                    Log.i(TAG, remoteMessage.getData().get("extra"));

            }
        } else {
            //Normal notification without any "rcode" field.
            new NotificationService(getBaseContext()).sendNotification(remoteMessage);
        }
    }
    // [END receive_message]

}
