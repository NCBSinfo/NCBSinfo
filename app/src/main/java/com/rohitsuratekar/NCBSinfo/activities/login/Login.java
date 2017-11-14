package com.rohitsuratekar.NCBSinfo.activities.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.ok;

public class Login extends AppCompatActivity {

    @BindView(R.id.login_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.login_email)
    TextInputLayout emailLayout;
    @BindView(R.id.login_password)
    TextInputLayout passLayout;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private LoginViewModel viewModel;

    private boolean isBackgroundBusy = true;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setTitle(R.string.login);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        subscribe();

        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toggleScreen();

    }

    @OnClick(R.id.login_btn)
    public void validate() {
        if (emailLayout.getEditText() != null && passLayout.getEditText() != null) {
            emailLayout.setErrorEnabled(false);
            passLayout.setErrorEnabled(false);
            if (General.isValidEmail(emailLayout.getEditText().getText().toString())) {
                if (passLayout.getEditText().getText().toString().length() > 5) {
                    signIn();
                } else {
                    passLayout.setError(getString(R.string.short_password));
                }
            } else {
                emailLayout.setError(getString(R.string.invalid_email));
            }
        }
    }

    private void signIn() {
        toggleScreen();
        if (emailLayout.getEditText() != null && passLayout.getEditText() != null) {
            mAuth.signInWithEmailAndPassword(emailLayout.getEditText().getText().toString(), passLayout.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    toggleScreen();
                    if (task.isSuccessful()) {
                        toggleScreen();
                        viewModel.startLoadingProcess(getApplicationContext());
                    } else {
                        if (task.getException() != null) {
                            Log.error(task.getException());
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                emailLayout.setError(getString(R.string.invalid_user));
                                emailLayout.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                passLayout.setError(getString(R.string.invalid_password));
                                passLayout.requestFocus();
                            } catch (FirebaseNetworkException e) {
                                General.makeLongToast(getApplicationContext(), getString(R.string.network_issue));
                            } catch (Exception e) {
                                //  FirebaseCrash.report(new Exception(e.getLocalizedMessage()));
                                General.makeLongToast(getApplicationContext(), e.getLocalizedMessage());
                            }
                        } else {
                            //todo FirebaseCrash.report(new Exception(("Empty Exception during login.")));
                            General.makeLongToast(getApplicationContext(), "Something is wrong!");
                        }
                    }

                }
            });
        }
    }

    private void subscribe() {
        viewModel.getUserDataReceived().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        toggleScreen();
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            }
        });

        viewModel.getErrorOccured().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    toggleScreen();
                    new Builder(Login.this)
                            .setTitle(getString(R.string.oops))
                            .setMessage(s)
                            .setPositiveButton(ok, new OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
    }

    private void toggleScreen() {
        if (isBackgroundBusy) {
            progressBar.setVisibility(View.INVISIBLE);
            emailLayout.setEnabled(true);
            passLayout.setEnabled(true);
            loginBtn.setEnabled(true);
            loginBtn.setAlpha(1f);
            isBackgroundBusy = false;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            emailLayout.setEnabled(false);
            passLayout.setEnabled(false);
            loginBtn.setEnabled(false);
            loginBtn.setAlpha(0.6f);
            isBackgroundBusy = true;
        }
    }

    @OnClick(R.id.login_create)
    public void register() {

    }

    @OnClick(R.id.login_forgot)
    public void forgot() {
        if (emailLayout.getEditText() != null) {
            if (emailLayout.getEditText().getText().toString().length() == 0) {
                emailLayout.setError(getString(R.string.blank_email));
            } else if (!General.isValidEmail(emailLayout.getEditText().getText().toString())) {
                emailLayout.setError(getString(R.string.invalid_email));
            } else {
                emailLayout.setErrorEnabled(false);
                toggleScreen();
                mAuth.sendPasswordResetEmail(emailLayout.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        toggleScreen();
                        if (task.isSuccessful()) {
                            new Builder(Login.this)
                                    .setTitle(getString(R.string.done))
                                    .setMessage(getString(R.string.forgot_password_success, emailLayout.getEditText().getText().toString()))
                                    .setPositiveButton(ok, new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        } else {
                            if (task.getException() != null) {
                                Log.error(task.getException());
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    emailLayout.setError(getString(R.string.invalid_user));
                                    emailLayout.requestFocus();
                                } catch (FirebaseNetworkException e) {
                                    General.makeLongToast(getApplicationContext(), getString(R.string.network_issue));
                                } catch (Exception e) {
                                    //FirebaseCrash.report(new Exception(e.getLocalizedMessage()));
                                    General.makeLongToast(getApplicationContext(), e.getLocalizedMessage());
                                }
                            } else {
                                //TODO FirebaseCrash.report(new Exception(("Empty Exception during forgot password.")));
                                General.makeLongToast(getApplicationContext(), "Something is wrong!");
                            }
                        }
                    }
                });
            }
        }
    }
}
