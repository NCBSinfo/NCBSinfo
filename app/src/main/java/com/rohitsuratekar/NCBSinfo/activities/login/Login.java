package com.rohitsuratekar.NCBSinfo.activities.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.background.ServiceCentre;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends BaseActivity implements AppConstants {
    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.LOGIN;
    }

    public static final String INTENT = Login.class.getName();
    private final String TAG = getClass().getSimpleName();
    private ProgressDialog progress;
    FirebaseAuth mAuth;
    Preferences pref;

    //UI elements
    @BindView(R.id.button_login)
    Button loginButton;
    @BindView(R.id.edittext_signin_email)
    TextInputEditText email;
    @BindView(R.id.edittext_signin_password)
    TextInputEditText password;
    @BindView(R.id.input_layout_signin_email)
    TextInputLayout emailLayout;
    @BindView(R.id.input_layout_signin_pass)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_forgotPass_text)
    TextView forgotPass;

    int cancelProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //Initialization
        mAuth = FirebaseAuth.getInstance();
        pref = new Preferences(getBaseContext());
        progress = new ProgressDialog(Login.this);
        cancelProgress = 0;

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra(INTENT);
        if (userEmail != null) {
            email.setText(userEmail);
        }
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()) {
                    if (new General().isNetworkAvailable(getBaseContext())) {
                        new AlertDialog.Builder(Login.this)
                                .setTitle("Password reset")
                                .setMessage(getString(R.string.warning_password_reset, email.getText().toString()))
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        showProgressDialog();
                                        progress.setMessage("Sending email...");
                                        mAuth.sendPasswordResetEmail(email.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            hideProgressDialog();
                                                            new FirebaseErrors(getBaseContext(),
                                                                    task.getException(), Login.this, R.drawable.icon_sign)
                                                                    .dialogWarning();
                                                        } else {
                                                            hideProgressDialog();
                                                            redirectToInternet();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }//Network
                    else {
                        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new General().isNetworkAvailable(getBaseContext())) {
                    if (validateEmail() && validatePass()) {
                        showProgressDialog();
                        cancelProgress = 0;
                        runnable.run(); //Start timer for connection timeout
                        progress.setMessage("Signing in...");

                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {
                                            Log.e(TAG, task.getException().getLocalizedMessage());
                                            hideProgressDialog();
                                            handler.removeCallbacks(runnable);
                                            FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), Login.this);
                                            String warning = firebaseErrors.getWarningMessage();
                                            String type = firebaseErrors.getType();
                                            switch (type) {
                                                case FirebaseErrors.INVALID_USER:
                                                    emailLayout.setError(warning);
                                                    requestFocus(email);
                                                    new AlertDialog.Builder(Login.this)
                                                            .setTitle(email.getText().toString() + " ?")
                                                            .setMessage(warning)
                                                            .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent intent = new Intent(Login.this, Registration.class);
                                                                    intent.putExtra(Registration.INTENT, email.getText().toString());
                                                                    startActivity(intent);
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
                                                case FirebaseErrors.WRONG_PASSWORD:
                                                    passwordLayout.setError(warning);
                                                    requestFocus(password);
                                                    break;
                                                case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                                    firebaseErrors.dialogWarning();
                                                    break;
                                                default:
                                                    firebaseErrors.dialogWarning();
                                            }
                                        } else {
                                            pref.clearAll();
                                            Database.getInstance(getBaseContext()).restartDatabase();
                                            //Reset App Data
                                            Intent service = new Intent(Login.this, ServiceCentre.class);
                                            service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_APP_DATA);
                                            startService(service);
                                            //TODO: network operations

                                            //Put all shared preferences
                                            pref.app().setMode(modes.ONLINE);
                                            pref.user().setEmail(email.getText().toString());
                                            pref.user().setAuthorization(loginStatus.SUCCESS);
                                            pref.user().setUserType(userType.OLD_USER);
                                            pref.user().setToken(FirebaseInstanceId.getInstance().getToken());
                                            Intent intent = new Intent(Login.this, OnlineHome.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                                        }

                                    }
                                }
                        );

                    }
                }//Network Available
                else {
                    Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
            progress = new ProgressDialog(Login.this);
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

    private void redirectToInternet() {

        new AlertDialog.Builder(Login.this)
                .setTitle("Sent!")
                .setIcon(R.drawable.icon_corect)
                .setMessage("We have successfully sent password reset link to your email address, do you want to visit your inbox ?")
                .setPositiveButton("Lets go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String url = "https://" + email.getText().toString().split("@")[1];
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

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
}
