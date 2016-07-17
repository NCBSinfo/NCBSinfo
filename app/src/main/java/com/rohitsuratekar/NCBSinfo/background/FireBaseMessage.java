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
    }
    // [END receive_message]

}

