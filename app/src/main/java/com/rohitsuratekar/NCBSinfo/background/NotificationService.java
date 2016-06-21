package com.rohitsuratekar.NCBSinfo.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class NotificationService implements UserInformation, NetworkConstants {

    public static final String NOTIFICATION_SENT = "sent";

    private Context context;

    public NotificationService(Context context) {
        this.context = context;
    }

    public void sendNotification(String title, String notificationMessage, Class c) {
        int requestID = (int) System.currentTimeMillis();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(context, c);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(sound)
                .setContentText(notificationMessage).setAutoCancel(true)
                .setContentIntent(contentIntent);
        notifySystem(mBuilder);
    }

    public void sendNotification(RemoteMessage remoteMessage) {
        int requestID = (int) System.currentTimeMillis();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(context, OnlineHome.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title, message, extra;
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            extra = keys.values.EXTRA_PERSONAL;
        } else {
            title = remoteMessage.getData().get(keys.TITLE);
            message = remoteMessage.getData().get(keys.MESSAGE);
            if (remoteMessage.getData().get(keys.EXTRA) != null) {
                extra = remoteMessage.getData().get(keys.EXTRA);
            } else {
                extra = keys.values.EXTRA_PERSONAL;
            }
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(sound)
                .setContentText(message).setAutoCancel(true)
                .setContentIntent(contentIntent);
        notifySystem(mBuilder);

        //Add to notification data
        NotificationModel note = new NotificationModel();
        note.setTimestamp(new Utilities().timeStamp());
        note.setTitle(title);
        note.setMessage(message);
        note.setFrom(remoteMessage.getFrom());
        note.setExtraVariables(extra);
        new NotificationData(context).add(note);
    }

    private void notifySystem(NotificationCompat.Builder mBuilder) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
