package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.secretbiology.retro.google.form.Commands;
import com.secretbiology.retro.google.form.Service;

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

        if(trigger!=null) {
            switch (trigger) {
                case REGISTER:
                    userRegistration();
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
}
