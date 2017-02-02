package com.rohitsuratekar.NCBSinfo.background.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

public class NotificationService {

    private Context context;
    private AppPrefs prefs;

    public NotificationService(Context context) {
        this.context = context;
        this.prefs = new AppPrefs(context);
    }

    public void send(int notificationNumber, String title, String message, PendingIntent contentIntent) {
        notifySystem(getBuilder(title, message, contentIntent), notificationNumber);
    }

    private NotificationCompat.Builder getBuilder(String title, String Message, PendingIntent contentIntent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSound(sound)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(General.getColor(context, R.color.colorPrimary))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setContentText(Message).setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{0, 500});
    }

    private void notifySystem(NotificationCompat.Builder mBuilder, int notificationNumber) {
        if (prefs.isNotificationAllowed()) {
            if (prefs.isUserLoggedIn()) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationNumber, mBuilder.build());
            } else {
                Log.inform("Notification not sent because user is not logged in");
            }
        } else {
            Log.inform("Notification not sent due to user preference");
        }
    }
}
