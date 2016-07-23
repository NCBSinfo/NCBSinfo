package com.rohitsuratekar.NCBSinfo.background;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class FireBaseMessage extends FirebaseMessagingService implements NetworkConstants {

    private final String TAG = getClass().getSimpleName();

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "FCM message received" + remoteMessage.getData());

        if (remoteMessage.getData().get(fcmKeys.RCODE) != null) {
            //Special action by with "rcode" field
            switch (remoteMessage.getData().get(fcmKeys.RCODE)) {
                case fcmTriggers.NEW_UPDATE:
                    if (remoteMessage.getData().get(fcmKeys.MESSAGE) != null && remoteMessage.getData().get(fcmKeys.TITLE) != null) {
                        new NotificationService(getBaseContext())
                                .updateNotification(remoteMessage.getData().get(fcmKeys.TITLE), remoteMessage.getData().get(fcmKeys.MESSAGE));
                    }
                    break;
                case fcmTriggers.DATA_SYNC:
                    Intent service = new Intent(getBaseContext(), NetworkOperations.class);
                    service.putExtra(NetworkOperations.INTENT, NetworkOperations.ALL_DATA);
                    startService(service);
                    break;
            }
        } else {
            //Normal notification without any "rcode" field.
            new NotificationService(getBaseContext()).sendNotification(remoteMessage);
        }

    }
    // [END receive_message]

}

