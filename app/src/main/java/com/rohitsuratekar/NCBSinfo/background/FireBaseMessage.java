package com.rohitsuratekar.NCBSinfo.background;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;

public class FireBaseMessage extends FirebaseMessagingService implements NetworkConstants {

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().get(keys.RCODE) != null) {
            //Special action by with "rcode" field
            switch (remoteMessage.getData().get(keys.RCODE)) {
                case triggers.NEW_UPDATE:
                    if (remoteMessage.getData().get(keys.MESSAGE) != null && remoteMessage.getData().get(keys.TITLE) != null) {
                        new NotificationService(getBaseContext())
                                .updateNotification(remoteMessage.getData().get(keys.TITLE), remoteMessage.getData().get(keys.MESSAGE));
                    }
                    break;
                case triggers.DATA_SYNC:
                    Intent service = new Intent(getBaseContext(), NetworkOperations.class);
                    service.putExtra(NetworkOperations.INTENT, NetworkOperations.RESEARCH_TALKS);
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
