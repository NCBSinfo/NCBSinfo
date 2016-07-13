package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.activities.events.Events;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.background.remote.RemoteModel;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.secretbiology.retro.google.form.Commands;
import com.secretbiology.retro.google.form.Service;
import com.secretbiology.retro.google.fusiontable.reponse.RowModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This service is to request network calls in background.
 * Use this to send data to Fusiontable or Google forms.
 * For Firebase , use DataManagement service.
 * This can be triggered by alarm manager
 */
public class NetworkOperations extends IntentService implements NetworkConstants, AlarmConstants, AppConstants {

    //Public Constants
    public static final String INTENT = NetworkOperations.class.getName();
    public static final String ALL_DATA = "all_data";
    public static final String REMOTE_DATA = "remoteData";

    public static final String REGISTER = "register";
    public static final String RESEARCH_TALKS = "research_talks";
    public static final int ACTIONCODE_RETRIEVED = 1;
    public static final int ACTIONCODE_UPDATED = 2;
    public static final int ACTIONCODE_NOTIFIED = 3;


    //Local constants
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private Preferences pref;

    public NetworkOperations() {
        super(NetworkOperations.class.getName());
    }

    public NetworkOperations(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Network Service started");
        this.context = getBaseContext();
        this.pref = new Preferences(getBaseContext());
        String trigger = intent.getStringExtra(INTENT);
        researchTalk();
        //Do not add default field here
        if (trigger != null) {
            switch (trigger) {
                case REGISTER:
                    userRegistration();
                    break;
                case RESEARCH_TALKS:
                    researchTalk();
                    break;
                case ALL_DATA:
                    fetchAllData();
                    break;
                case REMOTE_DATA:
                    remoteData();
                    break;
            }
        }

    }

    private void userRegistration() {
        Log.d(TAG, "Registration Process Started");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Subscribe to general topics
        FirebaseMessaging.getInstance().subscribeToTopic(fcmTopics.PUBLIC);
        FirebaseMessaging.getInstance().subscribeToTopic(fcmTopics.EMERGENCY);
        Log.d(TAG, "Subscribed with topic");

        if (mAuth.getCurrentUser() != null) {
            Commands formservice = Service.createService(Commands.class);
            Call<ResponseBody> call = formservice
                    .submitRegistration(
                            pref.user().getName(),
                            pref.user().getEmail(),
                            pref.user().getToken(),
                            mAuth.getCurrentUser().getUid(),
                            pref.user().getUserType().getValue(),
                            "Submit");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, response.body().toString());
                    //TODO: network details
                    //pref.edit().putBoolean(netwrok.REGISTRATION_DETAILS_SENT, true).apply();
                    //Start fetching event details
                    researchTalk();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, t.getLocalizedMessage());
                }
            });


        }
    }

    private void researchTalk() {

        String sql_query = "SELECT * FROM " + tables.RESEARCH_TALK;
        com.secretbiology.retro.google.fusiontable.Commands Commands =
                com.secretbiology.retro.google.fusiontable.Service.createService
                        (com.secretbiology.retro.google.fusiontable.Commands.class);
        Call<RowModel> call = Commands.getPublicRows(sql_query, API_KEY);
        call.enqueue(new Callback<RowModel>() {
            @Override
            public void onResponse(Call<RowModel> call, Response<RowModel> response) {
                for (int i = 0; i < response.body().getRows().size(); i++) {
                    String timestamp = response.body().getRows().get(i).get(0);
                    String notificationtitle = response.body().getRows().get(i).get(1);
                    String date = response.body().getRows().get(i).get(2);
                    String time = response.body().getRows().get(i).get(3);
                    String venue = response.body().getRows().get(i).get(4);
                    String speaker = response.body().getRows().get(i).get(5);
                    String affliations = response.body().getRows().get(i).get(6);
                    String title = response.body().getRows().get(i).get(7);
                    String host = response.body().getRows().get(i).get(8);
                    String dataCode = response.body().getRows().get(i).get(9);
                    String dataAction = response.body().getRows().get(i).get(10);
                    int actionCode = ACTIONCODE_RETRIEVED;
                    TalkModel talk = new TalkModel(0, timestamp, notificationtitle, date, time, venue, speaker, affliations, title, host, dataCode, actionCode, dataAction);

                    if (Database.getInstance(getBaseContext()).isAlreadyThere(TalkData.TABLE_TALK, TalkData.TALK_TIMESTAMP, timestamp)) {
                        //If entry is already present in database, check for Datacode and is not empty
                        if (talk.getTimestamp().trim().length() != 0) {
                            if (!dataAction.equals("send")) {
                                //If datacode is other than "Send", Take appropriate actions
                                TalkModel oldEntry = new TalkData(getBaseContext()).getEntry(timestamp);
                                if (dataAction.equals("update")) {
                                    talk.setDataID(oldEntry.getDataID()); //Add DataID to new entry
                                    talk.setActionCode(ACTIONCODE_UPDATED); //Change action code
                                    new TalkData(getBaseContext()).update(talk); //Update entry
                                    Log.i(TAG, "Research Talk entry updated");
                                }
                                if (dataAction.equals("delete")) {
                                    new TalkData(getBaseContext()).delete(oldEntry); //Delete entry
                                    Log.i(TAG, "Research Talk entry deleted");
                                }
                            }
                        }
                    } else {
                        //Else create new entry if it is not empty
                        if (talk.getTimestamp().trim().length() != 0) {
                            new TalkData(getBaseContext()).addEntry(talk);
                            Log.i(TAG, "New Research Talk entry added");
                        }
                    }
                }

                //Give notification to user when they receive event data for the first time
                if (!pref.app().isFirstEventNotificationSent()) {
                    String title = "Check upcoming events";
                    String message = "Now you can check upcoming events at NCBS right from this app. You will receive notifications for events at NCBS";
                    Random random = new Random();
                    int m = random.nextInt(9999 - 1000) + 1000; //Random number to send separate notification
                    new NotificationService(getBaseContext()).sendNotification(title, message, Events.class, m);
                    pref.app().firstEventNotificationSent();
                }

                //Remove old entries
                new TalkData(context).removeOld();
                //start notification service
                Intent i = new Intent(NetworkOperations.this, Alarms.class);
                i.putExtra(Alarms.INTENT, AlarmConstants.alarmTriggers.SEND_UPCOMING.name());
                context.sendBroadcast(i);

            }

            @Override
            public void onFailure(Call<RowModel> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());

            }
        });
    }

    private void remoteData() {

        String sql_query = "SELECT * FROM " + tables.REMOTE_TABLE + " WHERE 'Key'='version'";
        com.secretbiology.retro.google.fusiontable.Commands Commands =
                com.secretbiology.retro.google.fusiontable.Service.createService
                        (com.secretbiology.retro.google.fusiontable.Commands.class);
        Call<RowModel> call = Commands.getPublicRows(sql_query, API_KEY);

        final List<RemoteModel> remoteData = new ArrayList<>();
        call.enqueue(new Callback<RowModel>() {
            @Override
            public void onResponse(Call<RowModel> call, Response<RowModel> response) {
                if (response.isSuccess()) {

                    for (int i = 0; i < response.body().getRows().size(); i++) {
                        RemoteModel data = new RemoteModel();
                        data.setStatus(response.body().getRows().get(i).get(0));
                        data.setTrigger(response.body().getRows().get(i).get(1));
                        data.setLevel(response.body().getRows().get(i).get(2));
                        data.setCounter(response.body().getRows().get(i).get(3));
                        data.setKey(response.body().getRows().get(i).get(4));
                        data.setValue(response.body().getRows().get(i).get(5));
                        data.setExtraParameter(response.body().getRows().get(i).get(6));
                        remoteData.add(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<RowModel> call, Throwable t) {
            }
        });

    }

    public void fetchAllData() {

        if (pref.app().getMode().getValue().equals(modes.ONLINE.getValue())) {
            researchTalk();
        }
    }

}