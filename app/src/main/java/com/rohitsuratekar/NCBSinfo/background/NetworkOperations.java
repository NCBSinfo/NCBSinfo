package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.secretbiology.retro.google.form.Commands;
import com.secretbiology.retro.google.form.Service;
import com.secretbiology.retro.google.fusiontable.reponse.RowModel;

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
public class NetworkOperations extends IntentService implements NetworkConstants, UserInformation {

    //Public Constants
    public static final String INTENT = "networkIntent";
    public static final String REGISTER = "register";
    public static final String RESEARCH_TALKS = "research_talks";
    public static final int ACTIONCODE_RETRIVED = 1;
    public static final int ACTIONCODE_UPDATED = 2;


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

            }

            @Override
            public void onFailure(Call<RowModel> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());

            }
        });
    }
}
