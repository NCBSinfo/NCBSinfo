package com.rohitsuratekar.NCBSinfo.background.services;

import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.notifications.Notifications;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.Calendar;

public class FCMService extends FirebaseMessagingService {

    public static final String DEFAULT_EXTRA = "default";


    //RCODE string. This is important to analyze notifications.
    // All messages without rcode will be sent directly as notifications.
    static final String RCODE = "rcode";
    static final String TITLE = "title";
    static final String MESSAGE = "message";
    static final String TIMESTAMP = "timestamp";
    static final String EXPIRES = "expires";
    static final String EXTRA = "extra";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.inform("Message received from Firebase Service");
        String messageCode = remoteMessage.getData().get(RCODE);
        if (messageCode != null) {
            //TODO
        } else {
            regularNotification(remoteMessage);
        }
    }

    void regularNotification(RemoteMessage remoteMessage) {
        int requestID = (int) System.currentTimeMillis();
        String title, message, extra, timestamp, expires;

        //All default values
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        expires = DateConverter.convertToString(calendar, "yyyy-MM-dd HH:mm:ss");
        timestamp = General.timeStamp();
        extra = DEFAULT_EXTRA;

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        } else {
            title = remoteMessage.getData().get(TITLE);
            message = remoteMessage.getData().get(MESSAGE);
            extra = Helper.assignDefaultIfNull(remoteMessage.getData().get(EXTRA), DEFAULT_EXTRA);
            timestamp = Helper.assignDefaultIfNull(remoteMessage.getData().get(TIMESTAMP), timestamp);
            expires = Helper.assignDefaultIfNull(remoteMessage.getData().get(EXPIRES), expires);
        }

        try {
            if (Calendar.getInstance().before(DateConverter.convertToCalender(ConverterMode.MONTH_FIRST, expires))) {
                //Add to notification data
                NotificationModel note = new NotificationModel();
                note.setTimestamp(timestamp);
                note.setTitle(title);
                note.setMessage(message);
                note.setFrom(remoteMessage.getFrom());
                note.setExtraVariables(extra);
                note.setExpires(expires);
                new NotificationData(getBaseContext()).add(note);

                //Prepare notification
                Intent notificationIntent;
                notificationIntent = new Intent(getBaseContext(), Notifications.class);
                notificationIntent.setAction(Notifications.ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                new NotificationService(getBaseContext()).send(requestID, title, message, contentIntent);
            } else {
                Log.inform("Message has expired");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
