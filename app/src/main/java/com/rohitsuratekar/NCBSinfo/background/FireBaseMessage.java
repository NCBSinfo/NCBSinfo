package com.rohitsuratekar.NCBSinfo.background;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class FireBaseMessage extends FirebaseMessagingService implements NetworkConstants {

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
<<<<<<< HEAD

=======
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
                case triggers.CHANGE_TRANSPORT:
                    Intent service2 = new Intent(getBaseContext(), ChangeTransport.class);
                    service2.putExtra(ChangeTransport.ROUTE_NO, remoteMessage.getData().get(keys.transport.ROUTE)  );
                    service2.putExtra(ChangeTransport.CHANGE_VALUE, remoteMessage.getData().get(keys.transport.ROUTE_VALUE));
                    service2.putExtra(ChangeTransport.VALIDITY, remoteMessage.getData().get(keys.transport.VALIDITY));
                    startService(service2);
            }
        } else {
            //Normal notification without any "rcode" field.
            new NotificationService(getBaseContext()).sendNotification(remoteMessage);
        }
>>>>>>> 967322440fd386589f53e2e9bfbdf152fcee8323
    }
    // [END receive_message]

}

