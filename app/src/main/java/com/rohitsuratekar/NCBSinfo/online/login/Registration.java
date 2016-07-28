package com.rohitsuratekar.NCBSinfo.online.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.ChangeTransport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.FirebaseErrors;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class Registration extends AppCompatActivity implements UserInformation {

    //Public
    private static String TAG = "RegisterFragment";
    private ProgressDialog progress;

    TextInputEditText username, email, password;
    TextInputLayout user_layout, email_layout, password_layout;
    SharedPreferences pref;
    private FirebaseAuth mAuth;
    Button registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(Registration.this);
        progress.setCanceledOnTouchOutside(false);


        //UI components
        registerBtn = (Button) findViewById(R.id.button_register);
        username = (TextInputEditText) findViewById(R.id.edittext_register_name);
        password = (TextInputEditText) findViewById(R.id.edittext_register_password);
        email = (TextInputEditText) findViewById(R.id.edittext_register_email);
        user_layout = (TextInputLayout) findViewById(R.id.input_layout_register_name);
        password_layout = (TextInputLayout) findViewById(R.id.input_layout_register_pass);
        email_layout = (TextInputLayout) findViewById(R.id.input_layout_register_email);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePass() && validateUser()) {
                    progress.setMessage("Registering ...");
                    progress.show();
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        progress.dismiss();
                                        Log.w(TAG, "createWithEmail", task.getException());
                                        FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Registration.this);
                                        String warning = firebaseErrors.getWarningMessage();
                                        String type = firebaseErrors.getType();
                                        switch (type) {
                                            case FirebaseErrors.INVALID_USER:
                                                user_layout.setError(warning);
                                                requestFocus(email);
                                                break;
                                            case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                                firebaseErrors.dialogWarning();
                                                break;
                                            case FirebaseErrors.USER_EXISTS:
                                                new android.support.v7.app.AlertDialog.Builder(Registration.this)
                                                        .setTitle(getString(R.string.warning_user_exists_title))
                                                        .setMessage(getString(R.string.warning_user_exists))
                                                        .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                startActivity(new Intent(Registration.this, Login.class));
                                                            }
                                                        })
                                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        })
                                                        .show();
                                                break;
                                            default:
                                                firebaseErrors.dialogWarning();
                                        }

                                    } else {
                                        progress.setMessage("Signing in...");
                                        pref.edit().clear().apply();
                                        setTransportValue(); //Reset transport values
                                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (!task.isSuccessful()) {
                                                    progress.dismiss();
                                                    FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Registration.this);
                                                    firebaseErrors.dialogWarning();
                                                } else {
                                                    progress.dismiss();
                                                    pref.edit().putString(registration.USERNAME, username.getText().toString()).apply();
                                                    pref.edit().putString(registration.EMAIL, email.getText().toString()).apply();
                                                    pref.edit().putBoolean(registration.REGISTERED, true).apply();
                                                    pref.edit().putString(USER_TYPE, currentUser.NEW_USER).apply();
                                                    pref.edit().putString(Home.MODE, Home.ONLINE).apply();
                                                    Intent intent = new Intent(Registration.this, OnlineHome.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        Button gotoSignin = (Button) findViewById(R.id.button_goto_signin);

        if (gotoSignin != null) {
            gotoSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Registration.this, Login.class));
                }
            });
        }

        //If user has registered in previous app versions
        if (pref.getBoolean("pref_registered", false)) {
            username.setText(pref.getString("pref_username", ""));
            email.setText(pref.getString("pref_email", ""));
        }


    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePass() {
        if (password.getText().toString().trim().isEmpty()) {
            password_layout.setError(getString(R.string.warning_registration_empty_password));
            requestFocus(password);
            return false;
        } else if (password.length() < 6) {
            password_layout.setError(getString(R.string.warning_registration_bad_password));
            requestFocus(password);
            return false;
        } else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUser() {
        if (username.getText().toString().trim().isEmpty()) {
            user_layout.setError(getString(R.string.warning_registration_empty_username));
            requestFocus(username);
            return false;
        } else {
            user_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((email.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())) {
            email_layout.setError(getString(R.string.warning_registration_invalid_email));
            requestFocus(email);
            return false;
        } else {
            email_layout.setErrorEnabled(false);
        }
        return true;
    }

    public void setTransportValue() {

        pref.edit().putString(TransportConstants.NCBS_IISC_WEEK, getResources().getString(R.string.def_ncbs_iisc_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_IISC_SUNDAY, getResources().getString(R.string.def_ncbs_iisc_sunday)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_WEEK, getResources().getString(R.string.def_iisc_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_SUNDAY, getResources().getString(R.string.def_iisc_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_WEEK, getResources().getString(R.string.def_ncbs_mandara_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_SUNDAY, getResources().getString(R.string.def_ncbs_mandara_sunday)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_WEEK, getResources().getString(R.string.def_mandara_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_SUNDAY, getResources().getString(R.string.def_mandara_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_WEEK, getResources().getString(R.string.def_ncbs_icts_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_SUNDAY, getResources().getString(R.string.def_ncbs_icts_sunday)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_WEEK, getResources().getString(R.string.def_icts_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_SUNDAY, getResources().getString(R.string.def_icts_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_CBL, getResources().getString(R.string.def_ncbs_cbl)).apply();
        pref.edit().putString(TransportConstants.BUGGY_NCBS, getResources().getString(R.string.def_buggy_from_ncbs)).apply();
        pref.edit().putString(TransportConstants.BUGGY_MANDARA, getResources().getString(R.string.def_buggy_from_mandara)).apply();

        pref.edit().putString(TransportConstants.CAMP_BUGGY_NCBS, getResources().getString(R.string.def_camp_buggy_ncbs)).apply();
        pref.edit().putString(TransportConstants.CAMP_BUGGY_MANDARA, getResources().getString(R.string.def_camp_buggy_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_MANDARA, getResources().getString(R.string.def_camp_shuttle_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_NCBS, getResources().getString(R.string.def_camp_shuttle_ncbs)).apply();
        pref.edit().putString(netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();
    }

}
