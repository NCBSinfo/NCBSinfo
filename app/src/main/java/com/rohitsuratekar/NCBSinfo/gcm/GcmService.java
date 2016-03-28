package com.rohitsuratekar.NCBSinfo.gcm;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.Activity_NotificationReceiver;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.helper.helper_GCM;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

import java.util.Calendar;


/**
 * Imported and Modified from "Google Samples"
 * Service used for receiving GCM messages.
 */
public class GcmService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    String notificationTitle;
    String currentTopic;



    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i("GCM Received", data.toString() );


        if (from.startsWith("/topics/")) {
            // message received from some topic.
            notificationTitle=data.getString("title");
            int pseudoIncrement=1;
            if (data.getString("rcode","0").equals("timed")){
                int seconds=0;
                String[] split1 = data.getString("value","0").split(":");
                if (split1.length==2){

                    seconds = new helper_GCM().converToSeconds(Integer.parseInt(split1[0]),Integer.parseInt(split1[1]));
                }
                else { seconds = 0;}
                currentTopic = from.replace("/topics/","");
                timedNotification(data.getString("message"),seconds);

            } else if (data.getString("rcode","0").equals("invisible")){

                  PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.START_FLAG, true).apply();
                  PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.FLAG_CODE, data.getString("value", "0")).apply();
                  sendNotification("Your admin access to NCBSinfo has expired!");
            }
            else {sendNotification(data.getString("message"));
                currentTopic = from.replace("/topics/","");
                String timestamp = new helper_GCM().timeStamp();
                DatabaseHelper db = new DatabaseHelper(this);
                db.addNotificationEntry(new models_userNotifications(pseudoIncrement, timestamp, data.getString("title"), data.getString("message"), currentTopic, 1)); //1 is to show Notification

            }

        } else {
            // normal downstream message.
            notificationTitle=data.getString("title");
            sendNotification(data.getString("message"));
        }

    }

    @Override
    public void onDeletedMessages() {
        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {
        sendNotification("Upstream message sent. Id=" + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.

    private void sendNotification(String notificationMessage) {

        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(getApplicationContext(), Activity_NotificationReceiver.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
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

    public void timedNotification(String notificationstring, int Seconds){

        Log.i("Set Timed", "Now");
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 30 seconds to the calendar object
        cal.add(Calendar.SECOND, Seconds);
        Intent intent = new Intent(getBaseContext(), TimedNotifications.class);
        intent.putExtra("message",notificationstring );
        intent.putExtra("title",notificationTitle);
        intent.putExtra("topic", currentTopic);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(), 1989, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }

}
