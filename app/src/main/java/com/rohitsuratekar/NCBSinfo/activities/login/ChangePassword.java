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
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class ChangePassword extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.CHANGE_PASSWORD;
    }

    //UI elements
    @BindView(R.id.input_layout_old_password)
    TextInputLayout oldPassLayout;
    @BindView(R.id.input_layout_new_pass)
    TextInputLayout newPassLayout;
    @BindView(R.id.input_layout_confirm_pass)
    TextInputLayout confirmPassLayout;
    @BindView(R.id.edittext_old_password)
    TextInputEditText oldPassword;
    @BindView(R.id.edittext_new_pass)
    TextInputEditText newPassword;
    @BindView(R.id.edittext_confirm_pass)
    TextInputEditText confirmPassword;
    @BindView(R.id.button_change_pass)
    Button changePassButton;
    @BindView(R.id.change_pass_email)
    TextView changePassEmail;
    @BindView(R.id.changePass_forgotPass_text)
    TextView forgotPass;


    FirebaseAuth mAuth;
    Preferences pref;
    ProgressDialog progress;
    int cancelProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        pref = new Preferences(getBaseContext());
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(ChangePassword.this);
        cancelProgress = 0;

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new General().isNetworkAvailable(getBaseContext())) {
                    new AlertDialog.Builder(ChangePassword.this)
                            .setTitle("Password reset")
                            .setMessage(getString(R.string.warning_password_reset, mAuth.getCurrentUser().getEmail()))
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showProgressDialog();
                                    progress.setMessage("Sending email...");
                                    mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        hideProgressDialog();
                                                        new FirebaseErrors(getBaseContext(),
                                                                task.getException(), ChangePassword.this, R.drawable.icon_sign)
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
        });

        //Check if user is logged in
        if (mAuth.getCurrentUser() != null) {

            changePassEmail.setText(mAuth.getCurrentUser().getEmail());
            changePassButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validatePass(oldPassLayout, oldPassword)) {
                        if (validatePass(newPassLayout, newPassword) && validatePass(confirmPassLayout, confirmPassword) && areBothSame()) {

                            showProgressDialog();
                            progress.setMessage("Authenticating...");
                            cancelProgress = 0;
                            runnable.run(); //Start timer for connection timeout


                            //First Sign in with old password
                            mAuth.signInWithEmailAndPassword(mAuth.getCurrentUser().getEmail(), oldPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        handler.removeCallbacks(runnable);
                                        Log.e(TAG, task.getException().getLocalizedMessage());
                                        hideProgressDialog();
                                        FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), ChangePassword.this);
                                        String warning = firebaseErrors.getWarningMessage();
                                        String type = firebaseErrors.getType();
                                        switch (type) {
                                            case FirebaseErrors.INVALID_USER:
                                                oldPassLayout.setError(warning);
                                                requestFocus(oldPassword);
                                                break;
                                            case FirebaseErrors.WRONG_PASSWORD:
                                                oldPassLayout.setError(warning);
                                                requestFocus(oldPassword);
                                                break;
                                            case FirebaseErrors.TOO_MANY_ATTEMPTS:
                                                firebaseErrors.dialogWarning();
                                                break;
                                            default:
                                                firebaseErrors.dialogWarning();
                                        }

                                    }//Not successful
                                    else {

                                        progress.setMessage("Changing password...");
                                        //Now change password
                                        mAuth.getCurrentUser().updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    handler.removeCallbacks(runnable);
                                                    hideProgressDialog();
                                                    FirebaseErrors firebaseErrors = new FirebaseErrors(getBaseContext(), task.getException(), ChangePassword.this);
                                                    firebaseErrors.dialogWarning();
                                                } else {
                                                    hideProgressDialog();
                                                    Toast.makeText(getBaseContext(), "Password changed successfully!", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                                                }
                                            }
                                        });
                                    }

                                }
                            }); //Sign in
                        }
                    }
                }
            });
        }

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePass(TextInputLayout layout, TextInputEditText field) {
        if (field.getText().toString().trim().isEmpty()) {
            layout.setError(getString(R.string.warning_registration_empty_password));
            requestFocus(field);
            return false;
        } else if (field.length() < 6) {
            layout.setError(getString(R.string.warning_registration_bad_password));
            requestFocus(field);
            return false;
        } else {
            layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean areBothSame() {
        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassLayout.setError(getString(R.string.warning_change_pass_do_not_match));
            requestFocus(confirmPassword);
            return false;
        } else {
            return true;
        }
    }

    private void showProgressDialog() {
        if (progress == null) {
            progress = new ProgressDialog(ChangePassword.this);
            progress.setMessage("Authenticating...");
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

    private void redirectToInternet() {

        new AlertDialog.Builder(ChangePassword.this)
                .setTitle("Sent!")
                .setIcon(R.drawable.icon_corect)
                .setMessage("We have successfully sent password reset link to your email address, do you want to visit your inbox ?")
                .setPositiveButton("Lets go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String url = "https://" + changePassEmail.getText().toString().split("@")[1];
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

}
