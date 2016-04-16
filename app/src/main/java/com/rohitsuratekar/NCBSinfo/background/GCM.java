package com.rohitsuratekar.NCBSinfo.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.EventDetails;
import com.rohitsuratekar.NCBSinfo.activity.EventUpdates;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.helpers.NetworkRelated;


public class GCM extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if (from.startsWith("/topics/")) {
            String topic = from.split("topics/")[1];
            switch (topic) {
                case Network.GCM_TOPIC_RESEARCHTALK:
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_GCM_RECEIVED, new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_RESEARCHTALK));
                    String datacode = data.getString(General.GEN_NOTIFICATION_DATACODE,"null");
                    String dataID = data.getString(General.GEN_NOTIFICATION_DATA_ID,"null");
                    sendNotification(data.getString("title"), data.getString("message"),datacode,dataID);
                    break;
                case Network.GCM_TOPIC_PUBLIC:
                    String rcode = data.getString(Network.GCM_CODE);
                    String codemessgae = "Action code: " + rcode;
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_PUBLIC_GCM, codemessgae);
                    if (rcode != null) {
                        if (rcode.equals(Network.GCM_TRIGGER_DATAFETCH)) {
                            Intent service = new Intent(getBaseContext(), DataFetch.class);
                            service.putExtra(General.GEN_SERIVICE_SWITCH, Network.NET_START_FETCHING);
                            startService(service);
                        } else if (rcode.equals(Network.GCM_TRIGGER_NEW_UPDATE)) {
                            updateNotification(data.getString("title"), data.getString("message"));
                        } else if (rcode.equals(Network.GCM_TRIGGER_DELETEENTRY)){
                            deleteEntry(data.getString("value", "null"));
                        }
                    }
                    break;
                default:
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_NO_TOPIC, topic);
                    break;
            }
        }
    }

    private void sendNotification(String title, String notificationMessage, String datacode, String dataID) {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent;
        if (datacode.equals("null")){
            notificationIntent = new Intent(getBaseContext(), EventUpdates.class);
        }
        else
        {notificationIntent = new Intent(getBaseContext(), EventDetails.class);
            notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATA_ID,Integer.parseInt(dataID));
            notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATACODE,datacode);}

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = getApplicationContext().getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setColor(color)
                .setContentText(notificationMessage).setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
        new LogEntry(getBaseContext(),StatusCodes.STATUS_EVENTNOTIFIED,title);
    }

    private void updateNotification (String title, String notificationMessage){

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String url = "";
        try {
            //Check whether Google Play store is installed or not:
            this.getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + appPackageName;
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = getApplicationContext().getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setColor(color)
                .setContentText(notificationMessage).setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

    }

    private void deleteEntry(String Timestamp){
        if(!Timestamp.equals("null")){
            Database db = new Database(getBaseContext());
            if(db.isAlreadyThere(SQL.TABLE_DATABASE,SQL.DATA_TIMESTAMP,SQL.DATA_TIMESTAMP)){
            db.deleteDataEntry(db.getDatabaseEntry(db.getIDbyTimeStamp(Timestamp)));}
            else{
                new LogEntry(getBaseContext(),StatusCodes.STATUS_ERROR_DELETING);
            }
        }
    }
}