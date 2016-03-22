package com.rohitsuratekar.NCBSinfo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.retro.gform.gform_FormCommands;
import com.rohitsuratekar.NCBSinfo.retro.gform.gform_service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_GCMRegistration extends AppCompatActivity {

    TextInputLayout inputLayoutName,inputLayoutEmail;
    EditText inputName, inputEmail;
    GoogleCloudMessaging gcm;
    GcmPubSub pubSub;
    String regId;
    Button registerButton;
    Switch sw_talk, sw_jc, sw_student;
    Boolean AlreadyRegistered;
    TextView regiNotice, regiPref;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_register_layout);

        //Check if user have already registered
        AlreadyRegistered = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_Registered, false);
        if(!AlreadyRegistered){
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_TALK, true).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_JC, false).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_students, true).apply();
        }

        progress = new ProgressDialog(Activity_GCMRegistration.this);

        //UI elements
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        registerButton = (Button)findViewById(R.id.RegisterButton);
        sw_talk = (Switch)findViewById(R.id.researchTalk_switch);
        sw_jc = (Switch)findViewById(R.id.JournalClub_switch);
        sw_student = (Switch)findViewById(R.id.studentActivities_switch);
        regiNotice = (TextView)findViewById(R.id.regiForm_notice);
        regiPref = (TextView)findViewById(R.id.regiForm_pref);


        //Change Layout if already registered
        if (AlreadyRegistered){
            RegisterBackground();
        }

        //Events for change in switch configurations

        sw_talk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_TALK, true).apply();
                    Log.i("TALK ==", "True");
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_TALK, false).apply();
                    Log.i("TALK ==", "False");
                }
            }
        });

        sw_jc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_JC, true).apply();
                    Log.i("JC ==", "True");
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_JC, false).apply();
                    Log.i("JC ==", "FALSE");
                }
            }
        });

        sw_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_students, true).apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_students, false).apply();
                }
            }
        });


    }

    public void submitForm(View arg0){
        if (!AlreadyRegistered) {
            if (validateName() && validateEmail()) {

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        Activity_GCMRegistration.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Warning!");

                // Setting Dialog Message
                alertDialog.setMessage("This feature is currently in beta testing.");

                // Setting OK Button
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        progress.setMessage("Registering...");
                        progress.setIndeterminate(true);
                        progress.show();
                        progress.setCancelable(false);
                        progress.setCanceledOnTouchOutside(false);
                        registerInBackground();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_USERNAME, inputName.getText().toString()).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_EMAIL, inputEmail.getText().toString()).apply();

                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        }
        else{

            final AlertDialog alertDialog = new AlertDialog.Builder(
                    Activity_GCMRegistration.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Warning!");

            // Setting Dialog Message
            alertDialog.setMessage("You won't be able to receive any further notifications");

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    progress.setMessage("Wait...");
                    progress.setIndeterminate(true);
                    progress.show();
                    unregister();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Invalid Name");
            requestFocus(inputName);
            return false;
        }
        else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((inputEmail.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches())){
            inputLayoutEmail.setError("Invalid Email");
            requestFocus(inputEmail);
            return false;
        }
        else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true ;
    }

    private void registerInBackground() {


        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
                    }
                    regId = InstanceID.getInstance(getBaseContext()).getToken(GCMConstants.GOOGLE_PROJECT_ID,GCMConstants.GCMscope);

                    Log.d("RegisterActivity", "registerInBackground - regId: "+ regId);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(regId);
                } catch (IOException ex) {
                    progress.dismiss();
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), "Registered with GCM Server!", Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(final String regId) {
        gform_FormCommands Service = gform_service.createService(gform_FormCommands.class);
        Call<ResponseBody> call = Service.submitForm(inputName.getText().toString(), inputEmail.getText().toString(), regId,"Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccess()) {
                    Log.i("RETRO----", "Form Submitted"); // Got Token
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_REG_ID, regId).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_Registered, true).apply();
                    subscribeTopics(regId);

                } else {
                    progress.dismiss();
                    Log.i("RETRO----", response.raw().toString());
                    Log.i("RETRO----", response.message());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void RegisterBackground() {

        inputName.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_USERNAME, "N/A"));
        inputName.setEnabled(false);
        inputName.setFocusable(false);
        inputEmail.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_EMAIL, "N/A"));
        inputEmail.setEnabled(false);
        inputEmail.setFocusable(false);
        registerButton.setText("Unregister");
        if(!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_TALK, true)){
            sw_talk.setVisibility(View.GONE);}
        sw_talk.setEnabled(false);
        if (!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_students, true)){
            sw_student.setVisibility(View.GONE);}
        sw_student.setEnabled(false);
        if (!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_JC, false)){
            sw_jc.setVisibility(View.GONE);}
        sw_jc.setEnabled(false);
        regiNotice.setText("You are already registered with following details.");
        regiPref.setText("You will receive notifications for following topics.");

    }

    private void unregister() {


        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
                    }
                    InstanceID.getInstance(getBaseContext()).deleteToken(GCMConstants.GOOGLE_PROJECT_ID,GCMConstants.GCMscope);
                    Log.d("RegisterActivity", "Token Deleted");
                    sendDeleteFlag();

                } catch (IOException ex) {
                    progress.dismiss();
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {

                progress.dismiss();
                Toast.makeText(getApplicationContext(), "You are unregistered from all notifications", Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    private void sendDeleteFlag() {
        gform_FormCommands Service = gform_service.createService(gform_FormCommands.class);
        Call<ResponseBody> call = Service.submitForm(inputName.getText().toString(), inputEmail.getText().toString(),"Deleted","Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccess()) {
                    Log.i("RETRO----", "Form Submitted"); // Got Token
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_REG_ID).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_Registered).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_USERNAME).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_EMAIL).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_TALK).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_JC).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_students).apply();
                    Intent intent = new Intent(Activity_GCMRegistration.this,Home.class);
                    progress.setMessage("Almost done!");
                    startActivity(intent);
                } else {
                    progress.dismiss();
                    Log.i("RETRO----", response.raw().toString());
                    Log.i("RETRO----", response.message());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progress.dismiss();

                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void subscribeTopics(final String token) {


        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";


                try {
                    if (pubSub == null) {
                        pubSub= GcmPubSub.getInstance(getBaseContext());
                    }
                    pubSub.subscribe(token, "/topics/" + GCMConstants.GCM_TOPIC_EMERGENCY, null);
                    if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_TALK, true)){
                        pubSub.subscribe(token, "/topics/" + GCMConstants.GCM_TOPIC_TALK, null);}
                    if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_JC, false)){
                        pubSub.subscribe(token, "/topics/" + GCMConstants.GCM_TOPIC_jc, null);}
                    if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_students, true)){
                        pubSub.subscribe(token, "/topics/" + GCMConstants.GCM_TOPIC_student, null);}
                    Log.d("Topics", "Subscribed to topics");

                } catch (IOException ex) {
                    progress.dismiss();
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully subscribed!", Toast.LENGTH_LONG).show();
                RegisterBackground();
            }
        }.execute(null, null, null);
    }


}
