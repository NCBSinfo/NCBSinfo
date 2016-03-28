package com.rohitsuratekar.NCBSinfo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.helper.helper_AES;
import com.rohitsuratekar.NCBSinfo.helper.helper_GCM;
import com.rohitsuratekar.NCBSinfo.retro.gplus_response.Response_gplus;
import com.rohitsuratekar.NCBSinfo.retro.retro_Commands;
import com.rohitsuratekar.NCBSinfo.retro.retro_Data;
import com.rohitsuratekar.NCBSinfo.retro.retro_LoginService;
import com.rohitsuratekar.NCBSinfo.retro.retro_MakeQuery;
import com.rohitsuratekar.NCBSinfo.retro.retro_Response_AccessToken;
import com.rohitsuratekar.NCBSinfo.retro.retro_Response_RowUpdate;
import com.rohitsuratekar.NCBSinfo.retro.retro_Response_SpecificRowValue;
import com.rohitsuratekar.NCBSinfo.retro.retro_Response_Topic;
import com.rohitsuratekar.NCBSinfo.retro.retro_Services_Auth;
import com.rohitsuratekar.NCBSinfo.retro.retro_Services_FusionTable;
import com.rohitsuratekar.NCBSinfo.retro.retro_Services_GCM;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_GCMModeration extends AppCompatActivity {

    TextInputLayout inputLayoutTitle,inputLayoutBody;
    EditText inputTitle, inputBody, inputExtra;
    ProgressDialog progress;
    Spinner spinner;
    String topicNameSelected, notificationSendingTime, notificationRcode, ovverideall, rCodeValue;
    Button setTimeButton, sentButton;
    CheckBox timeCheck, overrideCheck;
    String clientID, clientSecret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_moderator_layout);
        Calendar c = Calendar.getInstance();
        ovverideall="random"; //To avoid nullReferenece
        notificationSendingTime = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
        RelativeLayout rLog = (RelativeLayout)findViewById(R.id.Mod_loginScreen);
        RelativeLayout rMod = (RelativeLayout)findViewById(R.id.Mod_sendScreen);
        Boolean isModerator = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_MODERATORLOGIN, false);
        if(isModerator){ //If person is moderator, show send notification screen
            if ( PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_Registered, false)) {
                rLog.setVisibility(View.GONE);
                rMod.setVisibility(View.VISIBLE);
                inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_noti_title);
                inputLayoutBody = (TextInputLayout) findViewById(R.id.input_layout_noti_body);
                inputTitle = (EditText) findViewById(R.id.input_noti_title);
                inputBody = (EditText) findViewById(R.id.input_noti_body);
                inputExtra = (EditText) findViewById(R.id.ExtraParameterBox);
                inputExtra.setVisibility(View.GONE);
                sentButton = (Button) findViewById(R.id.sendTopicMessage);
                inputTitle.addTextChangedListener(new MyTextWatcher(inputTitle));
                inputBody.addTextChangedListener(new MyTextWatcher(inputBody));
                setTimeButton = (Button) findViewById(R.id.setTimeButton);
                setTimeButton.setVisibility(View.INVISIBLE);
                spinner = (Spinner) findViewById(R.id.Topic_spinner);
                timeCheck = (CheckBox) findViewById(R.id.setTime_CheckBox);
                overrideCheck = (CheckBox) findViewById(R.id.overideCheck);
                overrideCheck.setVisibility(View.GONE);
                final List<String> topic_list = new helper_GCM().getTopics(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_TOPIC_CODE, "101"));
                List<String> formated_list = new ArrayList<>();
                for (int i = 0; i < topic_list.size(); i++) {
                    formated_list.add(new helper_GCM().getTopicStrings(topic_list.get(i)));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_gcm_topic_list, R.id.topic_list_item_text, formated_list);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("Adapter CLicked :", "no:" + topic_list.get(position));
                        topicNameSelected = topic_list.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                setTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;

                        mTimePicker = new TimePickerDialog(Activity_GCMModeration.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                if(selectedMinute<10){  //This is important else it will go as single digit number
                                Log.i("Time Set: ", selectedHour + ":0" + selectedMinute);
                                setTimeButton.setText(selectedHour + ":0" + selectedMinute);
                                notificationSendingTime = selectedHour + ":0" + selectedMinute;}
                                else{
                                    Log.i("Time Set: ", selectedHour + ":" + selectedMinute);
                                    setTimeButton.setText(selectedHour + ":" + selectedMinute);
                                    notificationSendingTime = selectedHour + ":" + selectedMinute;
                                }
                            }
                        }, hour, minute, false);//false if for NO 24 hour time
                        mTimePicker.show();
                    }
                });


                timeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            setTimeButton.setVisibility(View.VISIBLE);
                            notificationRcode = "timed";
                        } else {
                            setTimeButton.setVisibility(View.INVISIBLE);
                            notificationRcode = "nottimed";
                        }
                    }
                });

                overrideCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ovverideall = "doit";
                        } else {
                            setTimeButton.setVisibility(View.INVISIBLE);
                            ovverideall = "asdgjh";
                        }
                    }
                });

                if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_USER_EXTRA, "101").equals("16o198")) {
                    inputExtra.setVisibility(View.VISIBLE);
                    overrideCheck.setVisibility(View.VISIBLE);
                }
            } //Alreadyregister condition
            else{

                final AlertDialog alertDialog = new AlertDialog.Builder(
                        Activity_GCMModeration.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Register yourself");

                // Setting Dialog Message
                alertDialog.setMessage("In order to use this facility, you need to register to GCM first!");

                // Setting OK Button
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Intent intent = new Intent(Activity_GCMModeration.this,Activity_GCMRegistration.class);
                        startActivity(intent);
                    }
                });

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(Activity_GCMModeration.this,Activity_GCMRegistration.class);
                        startActivity(intent);
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }

        }
        else {  //Else Send it to login page
            rLog.setVisibility(View.VISIBLE);
            rMod.setVisibility(View.GONE);

            final WebView web = (WebView) findViewById(R.id.mod_loginWeb);
            web.getSettings().setJavaScriptEnabled(true);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(Activity_GCMModeration.this);
            alert.setTitle("Warning!");
            alert.setMessage("Restricted Access! Use access key to proceed.");
            alert.setView(edittext);

            alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    String seedValue = edittext.getText().toString();
                    try {
                        clientID = helper_AES.decrypt(seedValue, GCMConstants.MOD_CLIENT_ID);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DECODED_CLIENTID, clientID).apply();
                        clientSecret = helper_AES.decrypt(seedValue, GCMConstants.MOD_CLIENT_SECRET);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DECODED_SECRET, clientSecret).apply();
                        web.setWebViewClient(new MyWebViewClient());
                        web.loadUrl(GCMConstants.MOD_LOGINURL + "?scope=" + GCMConstants.MOD_SCOPE + "&client_id=" + clientID + "&response_type=code&access_type=offline&prompt=consent&redirect_uri=" + GCMConstants.MOD_REDIRECT_URI);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "Now stare at blank screen :)", Toast.LENGTH_LONG).show();

                    }


                }
            });

            alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                    startActivity(intent);
                }
            });

            alert.setCancelable(false);
            alert.show();


        }

    }

    public void submitButton(View arg0){
        if(validateName()){

            sentButton.setEnabled(false);
            progress=new ProgressDialog(Activity_GCMModeration.this);
            progress.show();
            progress.setMessage("Sending notification to users");
            progress.setCancelable(false);

            if (ovverideall.equals("doit")){

                String topic = GCMConstants.GCM_TOPIC_EMERGENCY;
                topicNameSelected = topic;
                retro_Data data = new retro_Data();
                data.setMessage(inputBody.getText().toString());
                data.setTitle("Alert!");
                data.setRcode("invisible");
                data.setValue(inputExtra.getText().toString());
                retro_MakeQuery query = new retro_MakeQuery();
                query.setData(data);
                query.setTo("/topics/" + topic);
                rCodeValue= inputExtra.getText().toString();
                sendTopicMessage(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_API_KEY, "401"), query);

            }

            else {

                String topic = topicNameSelected;
                retro_Data data = new retro_Data();
                data.setMessage(inputBody.getText().toString());
                data.setTitle(inputTitle.getText().toString());
                data.setRcode(notificationRcode);
                data.setValue(notificationSendingTime);
                rCodeValue= notificationSendingTime;
                retro_MakeQuery query = new retro_MakeQuery();
                query.setData(data);
                query.setTo("/topics/" + topic);
                sendTopicMessage(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_API_KEY, "401"), query);
                Log.i("Sent===", data.getRcode() + data.getValue());
                String[] split1 = data.getValue().split(":");
                int seconds = new helper_GCM().converToSeconds(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]));
                Log.i("DEBUG===",split1.length+" Actual seconds:"+seconds);

            }

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
        if (inputTitle.getText().toString().trim().isEmpty()) {
            inputLayoutTitle.setError("Invalid Title");
            requestFocus(inputTitle);
            return false;
        }
        else {
            inputLayoutTitle.setErrorEnabled(false);
        }
        if (inputBody.getText().toString().trim().isEmpty()) {
            inputLayoutBody.setError("Message can't be empty");

            return false;}
        else {
            inputLayoutBody.setErrorEnabled(false);
        }
        return true;
    }

    public void sendTopicMessage (String token, retro_MakeQuery data){
        retro_Commands Service = retro_Services_GCM.createService(retro_Commands.class, token);
        Call<retro_Response_Topic> call = Service.sendTopicMessage(data);
        call.enqueue(new Callback<retro_Response_Topic>() {
            @Override
            public void onResponse(Call<retro_Response_Topic> call, Response<retro_Response_Topic> response) {
                if (response.isSuccess()) {
                    String RefreshToken = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_REFRESH_TOKEN, "401");
                    RefreshAndSendLog(RefreshToken);
                } else {

                    Log.i("RETRO----", response.raw().toString());
                    Log.i("RETRO----", response.message());

                }
            }

            @Override
            public void onFailure(Call<retro_Response_Topic> call, Throwable t) {

            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://rohitsuratekar.co.in")) {

                String code = Uri.parse(url).getQueryParameter("code");
                Log.i("Loaded", code);
                retro_LoginService loginService = retro_Services_Auth.createService(retro_LoginService.class, clientID, clientSecret);
                Call<retro_Response_AccessToken> call = loginService.getAccessToken(clientID, clientSecret, code, "authorization_code", "force", GCMConstants.MOD_REDIRECT_URI);
                Log.i("Current_URL==", call.request().toString());
                progress = new ProgressDialog(Activity_GCMModeration.this);
                progress.setMessage("Attempting to log in...");
                progress.setIndeterminate(true);
                progress.show();

                call.enqueue(new Callback<retro_Response_AccessToken>() {
                    @Override
                    public void onResponse(Call<retro_Response_AccessToken> call, retrofit2.Response<retro_Response_AccessToken> response) {
                        if (response.isSuccess()) {
                            Log.i("RETRO TOKEN----", response.body().getAccessToken()); // Got Token
                            Log.i("RETRO REFRESH TOKEN----", response.body().getRefreshToken()); // Got Token
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_ACCESS_TOKEN, response.body().getAccessToken()).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_REFRESH_TOKEN, response.body().getRefreshToken()).apply();
                            progress.setMessage("Login Successful, authenticating email...");
                            getUserInfo(response.body().getAccessToken(),getBaseContext());
                        } else {
                            Log.i("RETRO----", response.raw().toString());
                            Log.i("RETRO----", response.message());
                            Toast.makeText(getBaseContext(),"Error:"+response.message(),Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<retro_Response_AccessToken> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG) .show();
                        progress.dismiss();
                    }
                });

            }
            return Uri.parse(url).getHost().contains("?code=");
        }
    }
    //Using access token because this is immediate call after token exchange
    public void getUserInfo(final String AccessToken, final Context context){

        retro_Commands gplusSerive = retro_Services_Auth.createService(retro_Commands.class, clientID, clientSecret, AccessToken);
        Call<Response_gplus> call2 = gplusSerive.getUserinfo(AccessToken);

        call2.enqueue(new Callback<Response_gplus>() {
            @Override
            public void onResponse(Call<Response_gplus> call, Response<Response_gplus> response) {
                if (response.isSuccess()) {
                    Log.i("RETRO----", "Email ID: " + response.body().getEmails().get(0).getValue() + " and Name: " + response.body().getDisplayName());
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.MOD_EMAIL, response.body().getEmails().get(0).getValue()).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.MOD_USERNAME, response.body().getDisplayName()).apply();
                    getRowsByValue(GCMConstants.TABLE_ID, AccessToken, "Email", response.body().getEmails().get(0).getValue(), context);
                    progress.setMessage("Authenticating email...");
                } else {
                    Toast.makeText(context, "Failed to get database.", Toast.LENGTH_LONG).show();
                    Log.i("RETRO----", String.valueOf(response.code()));

                }
            }
            @Override
            public void onFailure(Call<Response_gplus> call, Throwable t) {
                Toast.makeText(context, "Error:"+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("RETRO----", t.getMessage());
            }


        });


    }
    //Using access token because follow up call after exchange
    public void getRowsByValue(String TableID, String AccessToken, String Column, String Rowvalue, final Context context){
        String sql_query = "SELECT * FROM "+TableID+" WHERE "+Column+"='"+Rowvalue+"'";
        retro_Commands fusionService = retro_Services_Auth.createService(retro_Commands.class, clientID, clientSecret, AccessToken);
        Call<retro_Response_SpecificRowValue> call2 = fusionService.getSpecificRow(sql_query, AccessToken);

        call2.enqueue(new Callback<retro_Response_SpecificRowValue>() {
            @Override
            public void onResponse(Call<retro_Response_SpecificRowValue> call, Response<retro_Response_SpecificRowValue> response) {
                if (response.isSuccess()) {

                    Log.i("Retro===", "Size" + response.body().getRows().size());

                    if (response.body().getRows().size()==0){
                        progress.dismiss();

                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                Activity_GCMModeration.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Something is wrong");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your access to moderator's zone has been revoked! Please contact developers.");

                        // Setting OK Button
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                    else{
                        progress.dismiss();
                        Log.i("Retro===", "Loaded Successfully");

                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.GCM_TOPIC_CODE,  response.body().getRows().get(0).get(1)).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.GCM_API_KEY, response.body().getRows().get(0).get(2)).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.GCM_MAX_DAILYQUOTA, response.body().getRows().get(0).get(3)).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.GCM_USER_EXTRA, response.body().getRows().get(0).get(4)).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.GCM_USER_LOGTABLE, response.body().getRows().get(0).get(5)).apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.DATA_MODERATORLOGIN, true).apply();
                        Toast.makeText(context, "Successfully Loaded All Data", Toast.LENGTH_LONG);
                        Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                        startActivity(intent);

                    }



                } else {
                    Toast.makeText(context, "Failed to get database.", Toast.LENGTH_LONG).show();
                    Log.i("RETRO----", String.valueOf(response.raw().code()));
                    if (response.code() == 403){
                        progress.dismiss();
                        Log.i("RETRO----", "Not permitted");
                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                Activity_GCMModeration.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Oops");

                        // Setting Dialog Message
                        alertDialog.setMessage("You don't have access to moderator's zone!");

                        // Setting OK Button
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                CookieSyncManager.createInstance(getBaseContext());
                                CookieManager cookieManager = CookieManager.getInstance();
                                cookieManager.removeAllCookie();
                                Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<retro_Response_SpecificRowValue> call, Throwable t) {
                Toast.makeText(context, "Error:"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sendModeratorLog(){

        progress.setMessage("Almost done...");

        String timeStamp = new helper_GCM().timeStamp();
        String DailyStamp = new helper_GCM().ModDailyStamp();
        String ModEmail = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.MOD_EMAIL, "401");
        String NotificationTitle = inputTitle.getText().toString();
        String NotificationTopic = topicNameSelected;
        String NotificationExtra = notificationRcode + ":"+ rCodeValue;
        String modTable = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_USER_LOGTABLE, "401");
        String APIkey = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.GCM_API_KEY, "401");
        String AccessToken = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_ACCESS_TOKEN, "401");
        String sql_query = "INSERT INTO "+modTable+ "(timeStamp, moderatorEmail,notificationUse,NotificationTitle,NotificationExtra,NotificationTopic) VALUES ('"+timeStamp+"','"+ModEmail+"','"+DailyStamp+"','"+NotificationTitle+"','"+NotificationExtra+"','"+NotificationTopic+"')";
        retro_Commands fusionService = retro_Services_FusionTable.createService(retro_Commands.class, AccessToken);
        Call<retro_Response_RowUpdate> call = fusionService.sendLogrow(sql_query, APIkey);

        call.enqueue(new Callback<retro_Response_RowUpdate>() {
            @Override
            public void onResponse(Call<retro_Response_RowUpdate> call, Response<retro_Response_RowUpdate> response) {
                if (response.isSuccess()) {

                    Log.i("Moderator Log Sent ===", "Suceessful");
                    Toast.makeText(getBaseContext(), "Successfully sent !", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getBaseContext(), "Failed to get database.", Toast.LENGTH_LONG).show();
                    Log.i("RETRO----", String.valueOf(response.raw().code()));
                    if (response.code() == 403) {
                        progress.dismiss();
                        Log.i("RETRO----", "Not permitted");
                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                Activity_GCMModeration.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Oops");

                        // Setting Dialog Message
                        alertDialog.setMessage("You don't have access to moderator's zone!");

                        // Setting OK Button
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               Intent intent = new Intent(Activity_GCMModeration.this, Home.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }

                }
            }

            @Override
            public void onFailure(Call<retro_Response_RowUpdate> call, Throwable t) {

            }
        });

    }

    public void RefreshAndSendLog(String RefreshToken){
        progress.setMessage("Updating registration...");
        String clientID2 = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DECODED_CLIENTID, "401");

        String clientSecret2 = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DECODED_SECRET, "401");
        retro_LoginService loginService = retro_Services_Auth.createService(retro_LoginService.class, clientID2, clientSecret2);
        Call<retro_Response_AccessToken> call = loginService.ExchangeRefreshToken(clientID2, clientSecret2, RefreshToken, "refresh_token");
        call.enqueue(new Callback<retro_Response_AccessToken>() {
            @Override
            public void onResponse(Call<retro_Response_AccessToken> call, Response<retro_Response_AccessToken> response) {
                if (response.isSuccess()) {
                Log.i("Refreshed ===",response.body().toString());
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(GCMConstants.DATA_ACCESS_TOKEN, response.body().getAccessToken()).apply();
                sendModeratorLog();
                }
                else {

                    progress.dismiss();
                    Toast.makeText(getBaseContext(), "Something is wrong. Please report error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<retro_Response_AccessToken> call, Throwable t) {
                progress.dismiss();

            }
        });


    }
}
