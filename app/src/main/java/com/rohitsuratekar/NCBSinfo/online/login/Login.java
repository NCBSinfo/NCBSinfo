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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
import com.rohitsuratekar.NCBSinfo.common.utilities.FirebaseErrors;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class Login extends AppCompatActivity implements UserInformation {

    //Local
    private static String TAG = "SignInFragment";
    private ProgressDialog progress;
    private DatabaseReference mDatabase;

    Button signInButton, forgotpassButton;
    TextInputEditText username, password;
    TextInputLayout user_layout, password_layout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initialization
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progress = new ProgressDialog(Login.this);
        progress.setCanceledOnTouchOutside(false);

        //UI
        signInButton = (Button) findViewById(R.id.button_sign_in);
        forgotpassButton = (Button) findViewById(R.id.button_signin_forgot_pass);
        username = (TextInputEditText) findViewById(R.id.edittext_signin_username);
        password = (TextInputEditText) findViewById(R.id.edittext_signin_password);
        user_layout = (TextInputLayout) findViewById(R.id.input_layout_signin_user);
        password_layout = (TextInputLayout) findViewById(R.id.input_layout_signin_pass);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Utilities().isOnline(getBaseContext())) {
                    if (validateEmail() && validatePass()) {
                        progress.setMessage("Signing in ...");
                        progress.show();
                        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    progress.dismiss();
                                    Log.w(TAG, "signInWithEmail", task.getException());
                                    FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Login.this);
                                    String warning = firebaseErrors.getWarningMessage();
                                    String type = firebaseErrors.getType();
                                    switch (type) {
                                        case FirebaseErrors.INVALID_USER:
                                            user_layout.setError(warning);
                                            requestFocus(username);
                                            break;
                                        case FirebaseErrors.WRONG_PASSWORD:
                                            password_layout.setError(warning);
                                            requestFocus(password);
                                            break;
                                        case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                            firebaseErrors.dialogWarning();
                                            break;
                                        default:
                                            firebaseErrors.dialogWarning();
                                    }

                                } else {
                                    pref.edit().clear().apply();
                                    Database db = new Database(getBaseContext());
                                    db.restartDatabase(db.getWritableDatabase());
                                    pref.edit().putString(registration.EMAIL, username.getText().toString()).apply();
                                    pref.edit().putString(MODE, ONLINE).apply();
                                    pref.edit().putBoolean(registration.REGISTERED, true).apply();
                                    progress.dismiss();
                                    //Start service to retrieve user data
                                    pref.edit().putString(USER_TYPE, currentUser.OLD_USER).apply();
                                    Intent service = new Intent(Login.this, DataManagement.class);
                                    service.putExtra(DataManagement.INTENT, DataManagement.FETCH_FIREBASE_DATA);
                                    startService(service);
                                    //Start UI and let system run in background.
                                    Intent intent = new Intent(Login.this, OnlineHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    //Retrieve data from database here
                                }//Successful Login2
                            }
                        });
                    }//Valid information
                }//IsOnline
                else {
                    Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
                }
            }//On click
        });


        forgotpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()) {
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Password reset")
                            .setMessage(getString(R.string.warning_password_reset, username.getText().toString()))
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.sendPasswordResetEmail(username.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

            }
        });


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

    private boolean validateEmail() {

        if ((username.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())) {
            user_layout.setError(getString(R.string.warning_registration_invalid_email));
            requestFocus(username);
            return false;
        } else {
            user_layout.setErrorEnabled(false);
        }
        return true;
    }

}
