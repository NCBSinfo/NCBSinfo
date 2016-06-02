package com.rohitsuratekar.NCBSinfo.tempActivitites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.Alarms;
import com.rohitsuratekar.NCBSinfo.background.Notifications;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.helpers.NetworkRelated;
import com.rohitsuratekar.retro.google.form.Commands;
import com.rohitsuratekar.retro.google.form.Service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExternalRegistrations extends AppCompatActivity {

    ProgressDialog progress;
    GoogleCloudMessaging gcm;
    String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_registrations);
        registerInBackground();
    }

    private void registerInBackground() {
        progress = new ProgressDialog(ExternalRegistrations.this);
        progress.show();
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.registration_dialog1));

        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
                    }
                    regId = InstanceID.getInstance(getApplicationContext()).getToken(Network.EXTERNAL_PROJECT, Network.GCM_SCOPE);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_REGISTRATION_ERROR, msg);
                }

                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Preferences.PREF_EXTERNAL_REGISTRATION_TOKEN,regId).apply();
                subscribeTopics(regId, Network.CONFERENCE_TOPIC);
            }
        }.execute(null, null, null);
    }

    private void subscribeTopics(final String token, final String TopicName) {

        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";

                GcmPubSub pubSub= GcmPubSub.getInstance(getApplicationContext());

                try {
                    if (pubSub == null) {
                        pubSub= GcmPubSub.getInstance(getApplicationContext());
                    }

                    pubSub.subscribe(token, "/topics/" + TopicName, null);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    new LogEntry(getBaseContext(),StatusCodes.STATUS_REGISTRATION_ERROR,msg);
                }
                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                sendForm();
            }
        }.execute(null, null, null);
    }

    private void sendForm (){
        progress.setMessage(getString(R.string.registration_dialog2));
        Commands formservice = Service.createService(Commands.class);
        //Submit to external registration not regular
        Call<ResponseBody> call = formservice.submitExternalForm("Rohit", "rohitcs@ncbs.res.in", regId, "CAMP16", "Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccess()) {

                    startActivity(new Intent(getBaseContext(),Home.class));
                } else {
                    progress.dismiss();
                    String temdetails = "Failed with status code :"+response.code();
                    new LogEntry(getBaseContext(),StatusCodes.STATUS_SENDFORM_FAIL,temdetails);
                    Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}
