package com.rohitsuratekar.NCBSinfo.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.EventDetails;
import com.rohitsuratekar.NCBSinfo.activity.EventUpdates;
import com.rohitsuratekar.NCBSinfo.activity.JustNotify;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.helpers.NetworkRelated;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;


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
                case Network.GCM_TOPIC_JC:
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_GCM_RECEIVED, new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_JC));
                    String datacode2 = data.getString(General.GEN_NOTIFICATION_DATACODE,"null");
                    String dataID2 = data.getString(General.GEN_NOTIFICATION_DATA_ID,"null");
                    sendNotification(data.getString("title"), data.getString("message"),datacode2,dataID2);
                    break;
                case Network.CONFERENCE_TOPIC:
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_GCM_RECEIVED, new NetworkRelated().getTopicStrings(Network.CONFERENCE_TOPIC));
                    new External(data);
                    break;
                case Network.GCM_TOPIC_PUBLIC:
                    String rcode = data.getString(Network.GCM_CODE);
                    String codemessgae = "Action code: " + rcode;
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_PUBLIC_GCM, codemessgae);
                    if (rcode != null) {
                        switch (rcode) {
                            case Network.GCM_TRIGGER_DATAFETCH:
                                Intent service = new Intent(getBaseContext(), DataFetch.class);
                                service.putExtra(General.GEN_SERIVICE_SWITCH, Network.NET_START_FETCHING);
                                startService(service);
                                break;
                            case Network.GCM_TRIGGER_NEW_UPDATE:
                                updateNotification(data.getString("title"), data.getString("message"));
                                break;
                            case Network.GCM_TRIGGER_DELETEENTRY:
                                deleteEntry(data.getString("value", "null"));
                                break;
                            case Network.GCM_TOPIC_DEBUG:
                                //For developers. This will avoid other users to receive this notifications
                                debugFunction();
                                break;
                            case Network.GCM_TRIGGER_ADD_TALK_ENTRY:
                                TalkModel talkModel = new TalkModel();
                                talkModel.setTimestamp(data.getString("timestamp","01/05/2016 00:00:00"));
                                talkModel.setTime(data.getString("time","00:00:00"));
                                talkModel.setDate(data.getString("date","01/05/2016"));
                                talkModel.setNotificationTitle(data.getString("title","Special talk"));
                                talkModel.setTitle(data.getString("talk_title","Title of the talk"));
                                talkModel.setHost(data.getString("host","Dean's office"));
                                talkModel.setAffilication(data.getString("affiliation","University"));
                                talkModel.setDatacode(General.GEN_DATACODE_TALK);
                                talkModel.setSpeaker(data.getString("speaker","Speaker Name"));
                                talkModel.setVenue(data.getString("venue", "Seminar hall"));
                                addEntrybyGCM(talkModel);
                                break;
                            case Network.GCM_TRIGGER_JUST_NOTIFY:
                                sendNotification(data.getString("title"), data.getString("message"),Network.GCM_TRIGGER_JUST_NOTIFY,"null");
                                break;
                            default:
                                String temp = "Unknown code : " + rcode;
                                new LogEntry(getBaseContext(), StatusCodes.STATUS_NO_TOPIC, temp);
                                break;
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
        switch (datacode) {
            case "null":
                notificationIntent = new Intent(getBaseContext(), EventUpdates.class);
                break;
            case Network.GCM_TRIGGER_JUST_NOTIFY:
                notificationIntent = new Intent(getBaseContext(), JustNotify.class);
                notificationIntent.putExtra(General.GEN_NOTIFY_TITLE, title);
                notificationIntent.putExtra(General.GEN_NOTIFY_MESSAGE, notificationMessage);
                break;
            default:
                notificationIntent = new Intent(getBaseContext(), EventDetails.class);
                notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATA_ID, Integer.parseInt(dataID));
                notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATACODE, datacode);
                break;
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = getApplicationContext().getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.notification_icon)
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
                .setSmallIcon(R.drawable.notification_icon)
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
            if(db.isAlreadyThere(SQL.TABLE_TALK,SQL.TALK_TIMESTAMP,Timestamp)){
            db.deleteTalkEntry(db.getTalkDataEntry(db.getTalkIDbyTimeStamp(Timestamp)));}
            else if (db.isAlreadyThere(SQL.TABLE_DATABASE,SQL.DATA_TIMESTAMP,Timestamp)){
             db.deleteDataEntry(db.getDatabaseEntry(db.getIDbyTimeStamp(Timestamp)));
            }
            else{
                new LogEntry(getBaseContext(),StatusCodes.STATUS_ERROR_DELETING);
            }
        }
    }

    private void addEntrybyGCM(TalkModel talkModel) {
        Database db = new Database(getBaseContext());
        db.addTalkEntry(talkModel);
        String entryDetails = talkModel.getNotificationTitle();
        new LogEntry(getBaseContext(),StatusCodes.STATUS_ENTRY_ADDED_GCM, entryDetails);
        Intent notservice=new Intent(getBaseContext(),Notifications.class);
        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        getBaseContext().sendBroadcast(notservice);
    }

    private void debugFunction() {
        //TODO function for developers, this will avoid other users from getting unnecessary notifications
    }
}