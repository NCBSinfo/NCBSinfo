package com.rohitsuratekar.NCBSinfo.tempActivitites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
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
    Button registerButton;
    EditText name, email, code;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCode;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String currentView = intent.getStringExtra(ExternalConstants.EXTERNAL_INTENT);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        switch (currentView){
            case ExternalConstants.CONFERENCE_CAMP2016:
                setContentView(R.layout.camp_registration);
                registerButton = (Button) findViewById(R.id.camp_regi_btn);
                name = (EditText) findViewById(R.id.campname);
                email = (EditText) findViewById(R.id.campemail);
                code = (EditText) findViewById(R.id.campcode);
                inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_campname);
                inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_campemail);
                inputLayoutCode = (TextInputLayout) findViewById(R.id.input_layout_campcode);
                break;
            default:
                setContentView(R.layout.external_registrations);
                registerButton = (Button)findViewById(R.id.external_regiBtn);
        }

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateName() && validateEmail() && validateCode()) {
                        registerInBackground();
                    }
                }
            });


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
                pref.edit().putString(Preferences.PREF_EXTERNAL_REGISTRATION_TOKEN,regId).apply();
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
                sendForm(ExternalConstants.CONFERENCE_CAMP2016);
            }
        }.execute(null, null, null);
    }

    private void sendForm (final String code){
        progress.setMessage(getString(R.string.registration_dialog2));
        Commands formservice = Service.createService(Commands.class);
        //Submit to external registration not regular
        Call<ResponseBody> call = formservice.submitExternalForm(name.getText().toString(), email.getText().toString(), regId, code, "Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccess()) {
                    new LogEntry(getBaseContext(),StatusCodes.STATUS_EXTERNAL_REGISTRATION,code);
                    if(code.equals(ExternalConstants.CONFERENCE_CAMP2016)) {
                        pref.edit().putBoolean(ExternalConstants.CAMP2016_REGISTERED, true).apply();
                        pref.edit().putString(ExternalConstants.CAMP2016_USERNAME, name.getText().toString()).apply();
                        pref.edit().putString(ExternalConstants.CAMP2016_EMAIL, email.getText().toString()).apply();
                        startActivity(new Intent(getBaseContext(), CAMP.class));
                    }
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getResources().getString(R.string.registration_error1));
            requestFocus(name);
            return false;
        }
        else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((email.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){
            inputLayoutEmail.setError(getString(R.string.registration_error2));
            requestFocus(email);
            return false;
        }
        else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true ;
    }

    private boolean validateCode() {
        if (!code.getText().toString().equals(Network.CAMP_REGISTRATION_CODE)) {
            inputLayoutCode.setError("Invalid Code. Please contact CAMP moderator.");
            requestFocus(code);
            return false;
        }
        else {
            inputLayoutCode.setErrorEnabled(false);
        }
        return true;
    }

}
