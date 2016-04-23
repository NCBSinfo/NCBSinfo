package com.rohitsuratekar.NCBSinfo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class Registration extends AppCompatActivity {

    TextView txt, gcmOptionMessage;
    Animation animFadein, animFadeOut;
    int animcounter;
    boolean aniBreak, buttonBreak;
    Button positive, negative, allset, nameDetailsButton;
    ImageView aniImage;
    LinearLayout aniLayout, gcmLayout, firstLayout, nameDetails;
    RelativeLayout anim_relativelayout;
    GoogleCloudMessaging gcm;
    String regId;
    ProgressDialog progress;
    CheckBox research, jc, student;
    TextInputLayout inputLayoutName,inputLayoutEmail;
    EditText inputName, inputEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //Change Registration preference

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_action_bar);

        animcounter = 0;
        aniBreak = true;
        buttonBreak=true;
        gcmOptionMessage = (TextView)findViewById(R.id.gcm_reg_option_message);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        research = (CheckBox)findViewById(R.id.check_research);
        jc = (CheckBox)findViewById(R.id.check_jc);
        student = (CheckBox)findViewById(R.id.check_studentactivities);
        positive = (Button)findViewById(R.id.first_PositiveButton);
        nameDetailsButton = (Button)findViewById(R.id.nameDetailsButton);
        negative = (Button)findViewById(R.id.first_negativeButton);
        allset = (Button)findViewById(R.id.gcm_all_setButton);
        txt = (TextView)findViewById(R.id.animation_text);
        aniImage = (ImageView) findViewById(R.id.animation_image);
        aniLayout = (LinearLayout)findViewById(R.id.animationLayout);
        gcmLayout = (LinearLayout)findViewById(R.id.first_gcm_options);
        firstLayout = (LinearLayout)findViewById(R.id.first_main_elements);
        nameDetails = (LinearLayout)findViewById(R.id.first_details_layout);
        anim_relativelayout = (RelativeLayout)findViewById(R.id.anim_relativelayout);
        gcmLayout.setVisibility(View.GONE);
        nameDetails.setVisibility(View.GONE);
        firstLayout.setVisibility(View.VISIBLE);
        anim_relativelayout.setVisibility(View.VISIBLE);
        positive.setVisibility(View.GONE);
        negative.setVisibility(View.GONE);
        aniImage.setVisibility(View.GONE);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);



        //Start animation if it is registration time visit
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_FIRSTTIME,true)){
        aniLayout.startAnimation(animFadein);}
        else { nameDetails.setVisibility(View.VISIBLE); firstLayout.setVisibility(View.GONE); }


         animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override  public void onAnimationStart(Animation animation) {
                if(animcounter==0){ txt.setText(getResources().getString(R.string.registration_salutation)); }
                else if (animcounter==1){
                    txt.setText(getResources().getString(R.string.registration_message1));
                    aniImage.setVisibility(View.VISIBLE);
                    aniImage.setBackgroundResource(R.drawable.icon_gift);
                }
                else if (animcounter==2){
                    txt.setText(getResources().getString(R.string.registration_message2));
                    aniImage.setBackgroundResource(R.drawable.icon_vibrate);
                }
                else if (animcounter==3){
                    txt.setText(getResources().getString(R.string.registration_message3));
                    aniImage.setBackgroundResource(R.drawable.icon_web);
                }
                else if (animcounter==4){
                    txt.setText(getResources().getString(R.string.registration_message4));
                    aniImage.setVisibility(View.GONE);
                    positive.setVisibility(View.VISIBLE);
                    negative.setVisibility(View.VISIBLE);
                    aniBreak=false;
                     }

            }
            @Override  public void onAnimationEnd(Animation animation) {
                if (aniBreak){aniLayout.startAnimation(animFadeOut);}
                else { aniLayout.clearAnimation();}

            }
            @Override  public void onAnimationRepeat(Animation animation) {  }
        });
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override  public void onAnimationStart(Animation animation) {  }
            @Override  public void onAnimationEnd(Animation animation) {
               if (aniBreak){
                   animcounter++;
                   aniLayout.startAnimation(animFadein);
               }
            }
            @Override  public void onAnimationRepeat(Animation animation) { }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogEntry(getBaseContext(), StatusCodes.STATUS_REGISTRATION_STARTED);
                Boolean tem = new NetworkRelated().isConnected(getBaseContext());
                if(tem){

                    aniLayout.clearAnimation();
                    animFadein.cancel();
                    animFadeOut.cancel();
                    aniLayout.setVisibility(View.GONE);
                    negative.setVisibility(View.GONE);
                    firstLayout.setVisibility(View.GONE);
                    nameDetails.setVisibility(View.VISIBLE);
                 }
                  else {
                    txt.setText("Oops!");
                    negative.setVisibility(View.GONE);
                    positive.setText("Retry!");
                    Snackbar.make(anim_relativelayout,getResources().getString(R.string.warning_internet_connection),Snackbar.LENGTH_LONG).show();
                  }

            }
        });

        allset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!research.isChecked() && !jc.isChecked() && !student.isChecked()) {

                        new AlertDialog.Builder(Registration.this)
                                .setTitle(getResources().getString(R.string.warning_in_hurry))
                                .setMessage(getResources().getString(R.string.warning_in_hurry_details))
                                .setPositiveButton(getResources().getString(R.string.warning_hurry_positive), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        allset.setEnabled(false);
                                        boolean[] sequence = {research.isChecked(), jc.isChecked(), student.isChecked()};
                                        registerInBackground(sequence);

                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.warning_hurry_negative), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_REGISTERED, false).apply();
                                        allset.setEnabled(false);
                                        startActivity(new Intent(getBaseContext(), Home.class));
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        allset.setEnabled(false);
                        progress = new ProgressDialog(getBaseContext());
                        progress.setCancelable(false);
                        progress.setMessage(getResources().getString(R.string.registration_dialog1));
                        boolean[] sequence = {research.isChecked(), jc.isChecked(), student.isChecked()};
                        registerInBackground(sequence);
                    }
                }
        });

        nameDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateName() && validateEmail()) {
                nameDetails.setVisibility(View.GONE);
                gcmLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogEntry(getBaseContext(), StatusCodes.STATUS_LOGINSKIP);
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_FIRSTTIME,false).apply();
                startActivity(new Intent(getBaseContext(),Home.class));
            }
        });

        //Change registration visit flag
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_FIRSTTIME,false).apply();

        //Check if use is already registered
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_REGISTERED,false)){
            nameDetails.setVisibility(View.GONE);
            gcmLayout.setVisibility(View.VISIBLE);
            research.setChecked(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_RESEARCHTALK,false));
            jc.setChecked(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_JC,false));
            student.setChecked(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_STUDENT,false));
            String user = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Preferences.PREF_USERNAME," ");
            gcmOptionMessage.setText(getResources().getString(R.string.registration_user_comeback,user));
            allset.setText(getResources().getString(R.string.registration_change_button));
        }


    }

     private void registerInBackground(final boolean[] codeSequence) {
         progress = new ProgressDialog(Registration.this);
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
                        regId = InstanceID.getInstance(getApplicationContext()).getToken(Network.GCM_PROJECT_ID, Network.GCM_SCOPE);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        new LogEntry(getBaseContext(), StatusCodes.STATUS_REGISTRATION_ERROR, msg);
                    }

                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Preferences.PREF_GCM_REGISTRATION_TOKEN,regId).apply();
                    subscribeTopics(regId, codeSequence);
            }
        }.execute(null, null, null);
    }

    private void subscribeTopics(final String token, final boolean[] codeSequence) {

        new AsyncTask<Object, Void, String>() {
            //@Override
            protected String doInBackground(Object... params) {
                String msg = "";

                GcmPubSub pubSub= GcmPubSub.getInstance(getApplicationContext());

                try {
                    if (pubSub == null) {
                        pubSub= GcmPubSub.getInstance(getApplicationContext());
                    }
                    pubSub.subscribe(token, "/topics/" + Network.GCM_TOPIC_PUBLIC, null);
                    pubSub.subscribe(token, "/topics/" + Network.GCM_TOPIC_URGENT, null);

                    if(codeSequence[0]){
                        pubSub.subscribe(token, "/topics/" + Network.GCM_TOPIC_RESEARCHTALK, null);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_RESEARCHTALK,true).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_SUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_RESEARCHTALK));}
                    else{
                        pubSub.unsubscribe(token, "/topics/" + Network.GCM_TOPIC_RESEARCHTALK);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_RESEARCHTALK,false).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_UNSUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_RESEARCHTALK));}

                    if(codeSequence[1]){
                        pubSub.subscribe(token, "/topics/" + Network.GCM_TOPIC_JC, null);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_JC,true).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_SUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_JC));}
                    else{
                        pubSub.unsubscribe(token, "/topics/" + Network.GCM_TOPIC_JC);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_JC,false).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_UNSUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_JC));}
                    if(codeSequence[2]){
                        pubSub.subscribe(token, "/topics/" + Network.GCM_TOPIC_STUDENT_ACTIVITY, null);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_STUDENT,true).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_SUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_STUDENT_ACTIVITY));}

                    else{
                        pubSub.unsubscribe(token, "/topics/" + Network.GCM_TOPIC_STUDENT_ACTIVITY);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_SUB_STUDENT,false).apply();
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_TOPIC_UNSUBSCRIBED,new NetworkRelated().getTopicStrings(Network.GCM_TOPIC_STUDENT_ACTIVITY));}

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    new LogEntry(getBaseContext(),StatusCodes.STATUS_REGISTRATION_ERROR,msg);
                }
                return msg;
            }

            //@Override
            protected void onPostExecute(String msg) {
                sendForm(codeSequence);

            }
        }.execute(null, null, null);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getResources().getString(R.string.registration_error1));
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
            inputLayoutEmail.setError(getString(R.string.registration_error2));
            requestFocus(inputEmail);
            return false;
        }
        else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true ;
    }

    private void sendForm (boolean[] codeSequence){
        progress.setMessage(getString(R.string.registration_dialog2));
        Commands formservice = Service.createService(Commands.class);
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_REGISTERED,false)){
            inputName.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Preferences.PREF_USERNAME,"Username"));
            inputEmail.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Preferences.PREF_EMAIL,"email"));
           }
        Call<ResponseBody> call = formservice.submitForm(inputName.getText().toString(), inputEmail.getText().toString(), regId, new NetworkRelated().getTopicCode(codeSequence), "Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccess()) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Preferences.PREF_GCM_REGISTRATION_TOKEN, regId).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.PREF_REGISTERED, true).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Preferences.PREF_USERNAME,  inputName.getText().toString()).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Preferences.PREF_EMAIL, inputEmail.getText().toString()).apply();
                    new LogEntry(getBaseContext(), StatusCodes.STATUS_USER_REGISTERED);
                    Intent startalarms=new Intent(getBaseContext(),Alarms.class);
                    startalarms.putExtra(General.GEN_ALARM_INTENT,General.GEN_START_ALARMS);
                    sendBroadcast(startalarms);
                    //Start daily alarms
                    Intent broadcast=new Intent(getBaseContext(),Alarms.class);
                    broadcast.putExtra(General.GEN_ALARM_INTENT,General.GEN_ALARM_DAILYRESET);
                    sendBroadcast(broadcast);
                    //Start daily notification service
                    Intent dailynotifications=new Intent(getBaseContext(),Notifications.class);
                    dailynotifications.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
                    sendBroadcast(dailynotifications);
                    //Send welcome notification
                    Intent notservice=new Intent(getBaseContext(),Notifications.class);
                    notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_SENDNOTIFICATION);
                    notservice.putExtra(General.GEN_NOTIFY_TITLE,"Welcome "+inputName.getText().toString());
                    notservice.putExtra(General.GEN_NOTIFY_MESSAGE,"You have successfully subscribed to event notifications :)");
                    sendBroadcast(notservice);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_REGISTERED,false)){
            startActivity(new Intent(this,Home.class));
        }
    }
}
