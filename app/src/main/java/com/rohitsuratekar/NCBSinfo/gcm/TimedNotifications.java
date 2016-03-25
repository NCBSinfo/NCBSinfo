package com.rohitsuratekar.NCBSinfo.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.Activity_NotificationReceiver;
import com.rohitsuratekar.NCBSinfo.helper.helper_GCM;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

public class TimedNotifications extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        int pseudoIncrement=1;
        Log.i("Received Timed Message", "Boardcasted");
        String notificationMessage = intent.getExtras().getString("message");
        String notificationTitle = intent.getExtras().getString("title");
        String topic = intent.getExtras().getString("topic");

        String timestamp = new helper_GCM().timeStamp();
        DatabaseHelper db = new DatabaseHelper(context);
        db.addNotificationEntry(new models_userNotifications(pseudoIncrement, timestamp, notificationTitle, notificationMessage, topic, 0)); //0 is to not show
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, Activity_NotificationReceiver.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = context.getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setColor(color)
                .setContentText(notificationMessage).setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }





}
