package com.rohitsuratekar.NCBSinfo.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.background.services.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    @BindView(R.id.regi_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.regi_name)
    TextInputLayout nameLayout;
    @BindView(R.id.regi_email)
    TextInputLayout emailLayout;
    @BindView(R.id.regi_password)
    TextInputLayout passwordLayout;
    @BindView(R.id.regi_confirm)
    TextInputLayout confirmLayout;
    @BindView(R.id.regi_btn)
    Button btn;

    private boolean isBackgroundBusy = true;
    private FirebaseAuth auth;
    private AppPrefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.regi_toolbar);
        setTitle(R.string.register);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        prefs = new AppPrefs(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        toggleScreen();
    }

    @OnClick(R.id.regi_btn)
    public void validate() {
        if (emailLayout.getEditText() != null && passwordLayout.getEditText() != null && nameLayout.getEditText() != null
                && confirmLayout.getEditText() != null) {
            emailLayout.setErrorEnabled(false);
            passwordLayout.setErrorEnabled(false);
            nameLayout.setErrorEnabled(false);
            confirmLayout.setErrorEnabled(false);
            if (nameLayout.getEditText().getText().toString().trim().length() != 0) {
                if (General.isValidEmail(emailLayout.getEditText().getText().toString())) {
                    if (passwordLayout.getEditText().getText().toString().length() > 5) {
                        if (confirmLayout.getEditText().getText().toString().equals(passwordLayout.getEditText().getText().toString())) {
                            register(nameLayout.getEditText().getText().toString().trim(),
                                    emailLayout.getEditText().getText().toString(), passwordLayout.getEditText().getText().toString());
                        } else {
                            confirmLayout.setError(getString(R.string.conf_pass_error));
                        }
                    } else {
                        passwordLayout.setError(getString(R.string.short_password));
                    }
                } else {
                    emailLayout.setError(getString(R.string.regi_email_error));
                }
            } else {
                nameLayout.setError(getString(R.string.name_error));
            }
        }
    }

    private void register(final String name, final String email, String password) {
        toggleScreen();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                toggleScreen();
                if (task.isSuccessful()) {
                    prefs.setUserEmail(email);
                    prefs.setUserName(name);
                    prefs.userLoggedIn();
                    Log.inform("User successfully created.");
                    CommonTasks.syncUserDetails(getApplicationContext());
                    CommonTasks.syncRoutes(getApplicationContext());
                    Intent intent = new Intent(Register.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    if (task.getException() != null) {
                        Log.error(task.getException());
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            emailLayout.setError(getString(R.string.user_exists));
                            emailLayout.requestFocus();
                        } catch (FirebaseNetworkException e) {
                            General.makeLongToast(getApplicationContext(), getString(R.string.network_issue));
                        } catch (Exception e) {
                            Crashlytics.logException(new Exception(e.getLocalizedMessage()));
                            General.makeLongToast(getApplicationContext(), e.getLocalizedMessage());
                        }
                    } else {
                        Crashlytics.logException(new Exception(("Empty Exception during login.")));
                        General.makeLongToast(getApplicationContext(), "Something is wrong!");
                    }
                }
            }
        });
    }


    private void toggleScreen() {
        if (isBackgroundBusy) {
            progressBar.setVisibility(View.INVISIBLE);
            emailLayout.setEnabled(true);
            nameLayout.setEnabled(true);
            confirmLayout.setEnabled(true);
            passwordLayout.setEnabled(true);
            btn.setEnabled(true);
            btn.setAlpha(1f);
            isBackgroundBusy = false;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            emailLayout.setEnabled(false);
            nameLayout.setEnabled(false);
            confirmLayout.setEnabled(false);
            passwordLayout.setEnabled(false);
            btn.setEnabled(false);
            btn.setAlpha(0.6f);
            isBackgroundBusy = true;
        }
    }

    @OnClick(R.id.reg_login)
    public void gotoLogin() {
        startActivity(new Intent(this, Login.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
