package com.rohitsuratekar.NCBSinfo.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.online.dashboard.DashBoard;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMP;

public class NotificationService implements UserInformation, NetworkConstants {

    public static final String NOTIFICATION_SENT = "sent";

    private Context context;

    public NotificationService(Context context) {
        this.context = context;

        //Common part of notification builder


    }

    //Regular Notification
    public void sendNotification(String title, String notificationMessage, Class c) {
        int requestID = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, c);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(sound)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setContentText(notificationMessage).setAutoCancel(true)
                .setContentIntent(contentIntent);
        notifySystem(mBuilder);
    }

    //Notification from FCM
    public void sendNotification(RemoteMessage remoteMessage) {
        int requestID = (int) System.currentTimeMillis();
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

        //Add to notification data
        NotificationModel note = new NotificationModel();
        note.setTimestamp(new Utilities().timeStamp());
        note.setTitle(title);
        note.setMessage(message);
        note.setFrom(remoteMessage.getFrom());
        note.setExtraVariables(extra);
        new NotificationData(context).add(note);
        Log.i("Tag", "notification data added");

        Intent notificationIntent;
        if (note.getFrom().contains(topics.CAMP16)) {
            notificationIntent = new Intent(context, CAMP.class);
        } else {
            notificationIntent = new Intent(context, DashBoard.class);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(sound)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setAutoCancel(true)
                .setContentIntent(contentIntent);

        //Notify
        notifySystem(mBuilder);
    }

    //Update notification
    public void updateNotification(String title, String notificationMessage) {
        int requestID = (int) System.currentTimeMillis();

        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String url = "";
        try {
            //Check whether Google Play store is installed or not:
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + appPackageName;
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(sound)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setContentText(notificationMessage).setAutoCancel(true)
                .setContentIntent(contentIntent);

        notifySystem(mBuilder);

    }

    private void notifySystem(NotificationCompat.Builder mBuilder) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        //Notifications will be send only if user has not changed default value and it is not "offline" mode.
        if (pref.getBoolean(preferences.NOTIFICATIONS, true) && !pref.getString(MODE, ONLINE).equals(OFFLINE)) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
