package com.rohitsuratekar.NCBSinfo.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoard;
import com.rohitsuratekar.NCBSinfo.activities.events.Events;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteBuilder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

/**
 * All notifications should be handled by this class
 * All notifications should use one universal notifier 'notifySystem'
 * Notifications will be send only in following conditions
 * (1) No notification will be send in 'Offline' mode
 * (2) Research Talk notifications will be sent only in 'Online' mode
 * (3) FCM notifications will be send in all modes except 'Offline'. (FCM data can still be accessed in 'Offline' mode)
 */
public class NotificationService implements NetworkConstants, AppConstants, AlarmConstants {

    public static final String NOTIFICATION_CODE = "notificationCode";

    private Context context;
    private final String TAG = getClass().getSimpleName();
    private int notificationNumber = 1; //Default

    public NotificationService(Context context) {
        this.context = context;
        Log.i(TAG, "Notification service called at" + new General().timeStamp());
    }

    enum notificationType {GENERAL, FCM, TRANSPORT, EVENTS}

    //Regular Notification
    public void sendNotification(String title, String notificationMessage, Class c) {
        int requestID = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, c);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifySystem(getBuilder(title, notificationMessage, contentIntent), notificationNumber, notificationType.GENERAL);
    }

    //Notification from FCM
    public void sendNotification(RemoteMessage remoteMessage) {
        int requestID = (int) System.currentTimeMillis();
        String title, message, extra;
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            extra = fcmKeys.values.EXTRA_PERSONAL;
        } else {
            title = remoteMessage.getData().get(fcmKeys.TITLE);
            message = remoteMessage.getData().get(fcmKeys.MESSAGE);
            if (remoteMessage.getData().get(fcmKeys.EXTRA) != null) {
                extra = remoteMessage.getData().get(fcmKeys.EXTRA);
            } else {
                extra = fcmKeys.values.EXTRA_PERSONAL;
            }
        }

        //Add to notification data
        NotificationModel note = new NotificationModel();
        note.setTimestamp(new General().timeStamp());
        note.setTitle(title);
        note.setMessage(message);
        note.setFrom(remoteMessage.getFrom());
        note.setExtraVariables(extra);
        new NotificationData(context).add(note);

        Intent notificationIntent;
        notificationIntent = new Intent(context, DashBoard.class);
        notificationIntent.putExtra(DashBoard.INTENT, "true");

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifySystem(getBuilder(title, message, contentIntent), notificationNumber, notificationType.FCM);
    }

    //Event Notification
    public void sendNotification(int code) {
        int requestID = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, Events.class);
        notificationIntent.putExtra(Events.EVENT_CODE, String.valueOf(code));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        TalkModel talk = new TalkData(context).getEntry(code);
        if (talk != null) {
            if (talk.getActionCode() != NetworkOperations.ACTIONCODE_NOTIFIED) {
                notifySystem(getBuilder(talk.getNotificationTitle(), talk.getTitle(), contentIntent), notificationNumber, notificationType.EVENTS);
                talk.setActionCode(NetworkOperations.ACTIONCODE_NOTIFIED);
                new TalkData(context).update(talk); //Update event as notified to avoid further spam
            }
        }
    }

    //Notifications for alarms
    public void sendNotification(AlarmModel alarm) {
        int requestID = (int) System.currentTimeMillis();
        Routes route = new TransportHelper().getRoute(Integer.parseInt(alarm.getExtraParameter()));
        TransportRoute transport = new RouteBuilder(route, context).build();
        Intent notificationIntent = new Intent(context, Transport.class);
        notificationIntent.putExtra(Transport.INDENT, alarm.getExtraParameter());
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String title = "Your upcoming " + transport.getType();
        String message = new DateConverters().convertFormat(alarm.getExtraValue(), DateFormats.TIME_12_HOURS_STANDARD)
                + " " + transport.getOrigin().toUpperCase() + " - " + transport.getDestination().toUpperCase() + " " + transport.getType() +
                " is departing soon";
        notifySystem(getBuilder(title, message, contentIntent), notificationNumber, notificationType.TRANSPORT);
        //Delete alarm after notification
        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(Alarms.INTENT, alarmTriggers.DELETE_ALARM.name());
        intent.putExtra(Alarms.ALARM_KEY, String.valueOf(alarm.getId()));
        context.sendBroadcast(intent);

    }

    //Update notification
    public void updateNotification(String title, String notificationMessage) {
        int requestID = (int) System.currentTimeMillis();

        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
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
        notifySystem(getBuilder(title, notificationMessage, contentIntent), notificationNumber, notificationType.GENERAL);

    }

    //Multiple Notification
    public void sendNotification(String title, String notificationMessage, Class c, int notificationNumber) {
        int requestID = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, c);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifySystem(getBuilder(title, notificationMessage, contentIntent), notificationNumber, notificationType.GENERAL);
    }


    /**
     * Set properties for notification builder
     *
     * @param title         : title of notification
     * @param Message       : message
     * @param contentIntent : Pending intent
     * @return : Notification Builder
     */
    private NotificationCompat.Builder getBuilder(String title, String Message, PendingIntent contentIntent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSound(sound)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setContentText(Message).setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{0, 500});
    }

    /**
     * Notify to system
     *
     * @param mBuilder           :Notification Builder
     * @param notificationNumber : Notification Number
     */
    private void notifySystem(NotificationCompat.Builder mBuilder, int notificationNumber, notificationType type) {
        Preferences pref = new Preferences(context);
        //Notifications will be send only if user has not changed default value and it is not "offline" mode.
        if (pref.user().isNotificationAllowed()
                && !pref.app().getMode().equals(modes.UNKNOWN)
                && !pref.app().getMode().equals(modes.OFFLINE)) {

            boolean allowed = true;
            switch (type) {
                case EVENTS:
                    if (!pref.settings().isEventNotificationON()) {
                        allowed = false;
                    }
                    break;
                case FCM:
                    if (!pref.settings().isImportantNotificationON()) {
                        allowed = false;
                    }
                    break;
            }

            if (allowed) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationNumber, mBuilder.build());
            }


        } else {
            Log.i(TAG, "Notification denied because either switched of by user or offline mode is ON");
        }
    }

}