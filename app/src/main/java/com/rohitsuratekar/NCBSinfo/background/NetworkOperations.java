package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.interfaces.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.ConferenceData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.ConferenceModel;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.secretbiology.retro.google.form.Commands;
import com.secretbiology.retro.google.form.Service;
import com.secretbiology.retro.google.fusiontable.reponse.RowModel;

import java.util.ArrayList;
import java.util.List;

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
public class NetworkOperations extends IntentService implements NetworkConstants, UserInformation, AlarmConstants {

    //Public Constants
    public static final String INTENT = "networkIntent";
    public static final String ALL_DATA = "all_data";

    public static final String REGISTER = "register";
    public static final String RESEARCH_TALKS = "research_talks";
    public static final String CAMP16 = "camp16Data";
    public static final int ACTIONCODE_RETRIVED = 1;
    public static final int ACTIONCODE_UPDATED = 2;
    public static final int ACTIONCODE_NOTIFIED = 3;



    //Local constants
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private SharedPreferences pref;

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
        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String trigger = intent.getStringExtra(INTENT);

        if (trigger != null) {
            switch (trigger) {
                case REGISTER:
                    userRegistration();
                    break;
                case RESEARCH_TALKS:
                    researchTalk();
                    break;
                case CAMP16:
                    campData();
                    break;
                case ALL_DATA:
                    fetchAllData();
                    break;
            }
        }

    }

    private void userRegistration() {
        Log.d(TAG, "Registration Process Started");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Commands formservice = Service.createService(Commands.class);
            Call<ResponseBody> call = formservice
                    .submitRegistration(
                            pref.getString(registration.USERNAME, "Username"),
                            pref.getString(registration.EMAIL, "Email@domain.com"),
                            pref.getString(registration.FIREBASE_TOKEN, "null"),
                            mAuth.getCurrentUser().getUid(),
                            pref.getString(registration.USER_TYPE, FireBaseID.REGULAR_USER),
                            "Submit");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, response.body().toString());
                    pref.edit().putBoolean(netwrok.REGISTRATION_DETAILS_SENT, true).apply();
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
                    int actionCode = ACTIONCODE_RETRIVED;
                    TalkModel talk = new TalkModel(0, timestamp, notificationtitle, date, time, venue, speaker, affliations, title, host, dataCode, actionCode, dataAction);

                    if (new Database(getBaseContext()).isAlreadyThere(TalkData.TABLE_TALK, TalkData.TALK_TIMESTAMP, timestamp)) {
                        //If entry is already present in database, check for Datacode
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
                    } else {
                        //Else create new entry
                        new TalkData(getBaseContext()).addEntry(talk);
                        Log.i(TAG, "New Research Talk entry added");
                    }
                }

                //Give notification to user when they receive event data for the first time
                if (pref.getBoolean(firstTime.FIRST_NOTIFICATION_EVENTS, true)) {
                    String title = "Check upcoming events right from NCBSinfo app";
                    String message = "Now you will receive notifications for events at NCBS";
                    new NotificationService(getBaseContext()).sendNotification(title, message, Events.class);
                    pref.edit().putBoolean(firstTime.FIRST_NOTIFICATION_EVENTS, false).apply();
                }

                //start notification service
                Intent i = new Intent(NetworkOperations.this, Alarms.class);
                i.putExtra(Alarms.INTENT, SEND_UPCOMINGS );
                startService(i);

            }

            @Override
            public void onFailure(Call<RowModel> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());

            }
        });
    }

    private void campData() {

        FirebaseMessaging.getInstance().subscribeToTopic(topics.CAMP16);
        String sql_query = "SELECT * FROM " + tables.CAMP_TABLE;
        com.secretbiology.retro.google.fusiontable.Commands Commands =
                com.secretbiology.retro.google.fusiontable.Service.createService
                        (com.secretbiology.retro.google.fusiontable.Commands.class);
        Call<RowModel> call = Commands.getPublicRows(sql_query, API_KEY);

        call.enqueue(new Callback<RowModel>() {
            @Override
            public void onResponse(Call<RowModel> call, Response<RowModel> response) {
                if (response.isSuccess()) {

                    for (int i = 0; i < response.body().getRows().size(); i++) {
                        String conferenceCode = response.body().getRows().get(i).get(0);
                        String eventID = response.body().getRows().get(i).get(1);
                        String eventTitle = response.body().getRows().get(i).get(2);
                        String eventSpeaker = response.body().getRows().get(i).get(3);
                        String eventHost = response.body().getRows().get(i).get(4);
                        String startTime = response.body().getRows().get(i).get(5);
                        String endTime = response.body().getRows().get(i).get(6);
                        String date = response.body().getRows().get(i).get(7);
                        String venue = response.body().getRows().get(i).get(8);
                        String message = response.body().getRows().get(i).get(9);
                        String eventCode = response.body().getRows().get(i).get(10);
                        String updateCounter = response.body().getRows().get(i).get(11);

                        ConferenceModel entry = new ConferenceModel(0, new Utilities().timeStamp(), conferenceCode,
                                eventID, eventTitle, eventSpeaker, eventHost, startTime, endTime, date,
                                venue, message, eventCode, Integer.valueOf(updateCounter));

                        if (new Database(getBaseContext()).isAlreadyThere(ConferenceData.TABLE_CONFERENCE, ConferenceData.CONFERENCE_CODE, conferenceCode)) {
                            //If entry is already present in database, check for Datacode
                            List<ConferenceModel> allData = new ConferenceData(getBaseContext()).getAll();
                            List<ConferenceModel> refine1 = new ArrayList<ConferenceModel>();
                            for (ConferenceModel a : allData) {
                                if (a.getCode().equals(registration.camp16.events.EXTERNAL_CONSTANT)) {
                                    refine1.add(a);
                                }
                            }
                            boolean newdataFound = true;
                            for (ConferenceModel b : refine1) {

                                if (b.getEventID().equals(eventID)) {
                                    if (b.getUpdateCounter() != Integer.valueOf(updateCounter)) {
                                        entry.setId(b.getId());
                                        new ConferenceData(getBaseContext()).update(entry);
                                        newdataFound = false;
                                    } else {
                                        newdataFound = false;
                                    }
                                }
                            }
                            if (newdataFound) {
                                new ConferenceData(getBaseContext()).add(entry);
                            }
                        } else {
                            //Else create new entry
                            new ConferenceData(getBaseContext()).add(entry);
                            Log.i(TAG, "New Conference entry added");
                        }

                    }
                    pref.edit().putBoolean(firstTime.CAMP_EVENTS_FETCHED, true).apply();
                }
            }

            @Override
            public void onFailure(Call<RowModel> call, Throwable t) {
            }
        });

    }

    public void fetchAllData(){

        switch (pref.getString(MODE, ONLINE)){
            case ONLINE:
                researchTalk(); break;
            case CAMP16:
                campData(); break;
        }

    }

}
