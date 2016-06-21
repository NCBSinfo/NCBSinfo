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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.FirebaseErrors;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;

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
                                                    pref.edit().putString(Home.MODE, Home.ONLINE).apply();
                                                    startActivity(new Intent(Registration.this, OnlineHome.class));

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

}
