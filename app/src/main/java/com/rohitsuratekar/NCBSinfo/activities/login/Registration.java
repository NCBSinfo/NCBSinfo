package com.rohitsuratekar.NCBSinfo.activities.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/login/Registration.java
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
=======
import com.rohitsuratekar.NCBSinfo.background.ChangeTransport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.FirebaseErrors;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/online/login/Registration.java

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registration extends BaseActivity implements AppConstants {

    public static final String INTENT = Registration.class.getName();
    private final String TAG = getClass().getSimpleName();
    private ProgressDialog progress;
    Preferences pref;
    FirebaseAuth mAuth;

    //UI elements
    @BindView(R.id.button_register)
    Button registerBtn;
    @BindView(R.id.edittext_register_name)
    TextInputEditText username;
    @BindView(R.id.edittext_register_password)
    TextInputEditText password;
    @BindView(R.id.edittext_register_email)
    TextInputEditText email;
    @BindView(R.id.input_layout_register_name)
    TextInputLayout userLayout;
    @BindView(R.id.input_layout_register_pass)
    TextInputLayout passwordLayout;
    @BindView(R.id.input_layout_register_email)
    TextInputLayout emailLayout;
    @BindView(R.id.register_switchToLogin)
    TextView switchToLogin;
    int cancelProgress;

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.REGISTRATION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        pref = new Preferences(getBaseContext());
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(Registration.this);
        cancelProgress = 0;

        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

        final Intent intent = getIntent();
        String userEmail = intent.getStringExtra(INTENT);
        if (userEmail != null) {
            email.setText(userEmail);
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePass() && validateUser()) {
                    cancelProgress = 0;
                    runnable.run(); //Start timer for connection timeout
                    progress.setMessage("Registering ...");
                    showProgressDialog();
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        hideProgressDialog();
                                        handler.removeCallbacks(runnable);
                                        Log.w(TAG, "createWithEmail", task.getException());
                                        FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Registration.this);
                                        String warning = firebaseErrors.getWarningMessage();
                                        String type = firebaseErrors.getType();
                                        switch (type) {
                                            case FirebaseErrors.INVALID_USER:
                                                userLayout.setError(warning);
                                                requestFocus(email);
                                                break;
                                            case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                                firebaseErrors.dialogWarning();
                                                break;
                                            case FirebaseErrors.USER_EXISTS:
                                                new AlertDialog.Builder(Registration.this)
                                                        .setTitle(getString(R.string.warning_user_exists_title))
                                                        .setMessage(getString(R.string.warning_user_exists))
                                                        .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent1 = new Intent(Registration.this, Login.class);
                                                                intent1.putExtra(Login.INTENT, email.getText().toString());
                                                                startActivity(intent1);
                                                                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
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
<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/login/Registration.java
                                        pref.clearAll();
=======
                                        pref.edit().clear().apply();
                                        setTransportValue(); //Reset transport values
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/online/login/Registration.java
                                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (!task.isSuccessful()) {
                                                    handler.removeCallbacks(runnable);
                                                    hideProgressDialog();
                                                    FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Registration.this);
                                                    firebaseErrors.dialogWarning();
                                                } else {
                                                    hideProgressDialog();
                                                    pref.user().setName(username.getText().toString());
                                                    pref.user().setEmail(email.getText().toString());
                                                    pref.user().setAuthorization(loginStatus.SUCCESS);
                                                    pref.user().setUserType(userType.NEW_USER);
                                                    pref.app().setMode(modes.ONLINE);
                                                    //TODO: start network calls
                                                    Intent intent = new Intent(Registration.this, OnlineHome.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        //If user has registered in previous app versions
        SharedPreferences temp1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (temp1.getBoolean("pref_registered", false)) {
            username.setText(temp1.getString("pref_username", ""));
            email.setText(temp1.getString("pref_email", ""));
        }


    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateUser() {
        if (username.getText().toString().trim().isEmpty()) {
            userLayout.setError(getString(R.string.warning_registration_empty_username));
            requestFocus(username);
            return false;
        } else {
            userLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((email.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())) {
            emailLayout.setError(getString(R.string.warning_registration_invalid_email));
            requestFocus(email);
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
        }
        return true;
    }

<<<<<<< HEAD:app/src/main/java/com/rohitsuratekar/NCBSinfo/activities/login/Registration.java
    private boolean validatePass() {
        if (password.getText().toString().trim().isEmpty()) {
            passwordLayout.setError(getString(R.string.warning_registration_empty_password));
            requestFocus(password);
            return false;
        } else if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.warning_registration_bad_password));
            requestFocus(password);
            return false;
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void showProgressDialog() {
        if (progress == null) {
            progress = new ProgressDialog(Registration.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
        }
        progress.show();
    }

    private void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.hide();
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {

            if (cancelProgress > 2) {
                return;
            }
            if (cancelProgress == 1) {
                progress.setMessage("It is taking longer than expected...");
            } else if (cancelProgress == 2) {
                cancelProgress = cancelProgress + 1;
                stopProgress();
            }
            cancelProgress = cancelProgress + 1;

            handler.postDelayed(this, 8000);
        }
    };

    private void stopProgress() {
        hideProgressDialog();
        handler.removeCallbacks(runnable);
        Toast.makeText(getBaseContext(), "Unexpected delay.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
=======
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

>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b:app/src/main/java/com/rohitsuratekar/NCBSinfo/online/login/Registration.java
}
