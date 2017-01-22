package com.rohitsuratekar.NCBSinfo.activities.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoginSessionObject;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoginUser;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.views.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends BaseActivity {

    @BindView(R.id.log_in_email)
    InputView email;
    @BindView(R.id.log_in_pass)
    InputView password;
    @BindView(R.id.log_txt_forgot)
    TextView forgot;


    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);

        prefs = new AppPrefs(getBaseContext());
        //TODO
        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (General.isValidEmail(s.toString())) {
                    email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().length() > 5) {
                    password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.log_txt_new, R.id.log_img_new})
    public void gotoRegister() {
        startActivity(new Intent(this, Register.class));
        animateTransition();
    }

    @OnClick({R.id.log_txt_forgot, R.id.log_img_forgot})
    public void forgotPassword() {
        if (General.isValidEmail(email.getText())) {
            if (General.isNetworkAvailable(getBaseContext())) {
                new AlertDialog.Builder(Login.this)
                        .setTitle("Are you sure?")
                        .setMessage(getString(R.string.forgot_password_message, email.getText()))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog.show();
                                progressDialog.setMessage("Sending...");
                                mAuth.sendPasswordResetEmail(email.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            showSentDialog();
                                        } else {
                                            showErrorDialog(task.getException().getLocalizedMessage());
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            } else {
                General.makeLongToast(getBaseContext(), getString(R.string.network_error));
            }
        } else {
            email.getFocus(getBaseContext());
            email.setError(getString(R.string.email_forgot_password_warning));
        }
    }

    @OnClick(R.id.log_bt_done)
    public void login() {
        if (General.isNetworkAvailable(getBaseContext())) {
            if (validate()) {
                progressDialog.show();
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                //Start Loading
                LoginSessionObject object = new LoginSessionObject();
                object.setContext(getBaseContext());
                object.setEmail(email.getText());
                object.setPassword(password.getText());
                object.setmAuth(mAuth);
                new LoginUser(new LoginUser.OnDataRetrieved() {
                    @Override
                    public void updateDialog(int progress, String message) {
                        progressDialog.setProgress(progress);
                        progressDialog.setMessage(message);
                    }

                    @Override
                    public void showError(String message) {
                        progressDialog.dismiss();
                        showErrorDialog(message);
                    }

                    @Override
                    public void onTaskComplete() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }).execute(object);

            }
        } else {
            General.makeLongToast(getBaseContext(), getString(R.string.network_error));
        }
    }


    private boolean validate() {
        if (General.isValidEmail(email.getText())) {
            email.setErrorEnabled(false);
            if (password.getText().length() > 5) {
                return true;
            } else {
                password.getFocus(getBaseContext());
                password.setError(getString(R.string.password_invalid));
                return false;
            }

        } else {
            email.getFocus(getBaseContext());
            email.setError(getString(R.string.email_invalid));
            return false;
        }
    }

    private void showSentDialog() {
        new AlertDialog.Builder(Login.this)
                .setTitle("Done!")
                .setIcon(R.drawable.icon_done)
                .setMessage(getString(R.string.forgot_password_success, email.getText()))
                .setPositiveButton("Check Inbox", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://" + email.getText().split("@")[1];
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                        animateTransition();
                    }
                })
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(Login.this)
                .setTitle("Oops!")
                .setIcon(R.drawable.icon_error)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.LOGIN;
    }
}
