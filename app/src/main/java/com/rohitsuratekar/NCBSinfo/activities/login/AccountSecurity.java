package com.rohitsuratekar.NCBSinfo.activities.login;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.views.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static android.R.string.ok;

public class AccountSecurity extends BaseActivity {

    @BindView(R.id.change_pass_old)
    InputView oldPass;
    @BindView(R.id.change_pass_new)
    InputView newPass;
    @BindView(R.id.change_pass_confirm)
    InputView confirmPass;
    @BindView(R.id.change_pass_note)
    TextView note;

    private FirebaseAuth mAuth;
    private AppPrefs prefs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        prefs = new AppPrefs(getBaseContext());
        progressDialog = new ProgressDialog(AccountSecurity.this);
        progressDialog.setCancelable(false);
        note.setText(getString(R.string.change_password_for, prefs.getUserEmail()));
    }

    @OnClick(R.id.change_pass_btn)
    public void changePass() {

        if (validate()) {
            progressDialog.show();
            progressDialog.setMessage("Authenticating...");
            progressDialog.setCancelable(false);
            //Get Credentials
            AuthCredential credential = EmailAuthProvider.getCredential(prefs.getUserEmail(), oldPass.getText());
            if (mAuth.getCurrentUser() != null) {
                mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().updatePassword(newPass.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        exitActivity();
                                    } else {
                                        showErrorDialog(task.getException().getMessage());
                                    }

                                }
                            });

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                showErrorDialog(getString(R.string.old_wrong_password, prefs.getUserEmail()));
                            } catch (Exception e) {
                                showErrorDialog(e.getMessage());
                            }
                        }
                    }
                });
            }
        }

    }

    @OnClick(R.id.change_pass_forgot)
    public void sendForgotPass() {
        new Builder(AccountSecurity.this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.forgot_password_message, prefs.getUserEmail()))
                .setPositiveButton(ok, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.show();
                        progressDialog.setMessage("Sending...");
                        mAuth.sendPasswordResetEmail(prefs.getUserEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                }).setNegativeButton(cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();

    }

    private void exitActivity() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(AccountSecurity.this)
                .setCancelable(false)
                .setTitle("Done!")
                .setIcon(R.drawable.icon_done)
                .setMessage(getString(R.string.password_change_success))
                .setPositiveButton(ok, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.setLastPassUpdate(General.timeStamp());
                        dialog.dismiss();
                        finish();
                        animateTransition();
                    }
                }).show();
    }

    private void showSentDialog() {
        new AlertDialog.Builder(AccountSecurity.this)
                .setTitle("Done!")
                .setIcon(R.drawable.icon_done)
                .setMessage(getString(R.string.forgot_password_success, prefs.getUserEmail()))
                .setPositiveButton("Check Inbox", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://" + prefs.getUserEmail().split("@")[1];
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                        animateTransition();
                    }
                })
                .setNegativeButton(ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private boolean validate() {
        if (oldPass.getText().length() > 5) {
            oldPass.setErrorEnabled(false);
            if (newPass.getText().length() > 5) {
                newPass.setErrorEnabled(false);
                if (confirmPass.getText().equals(newPass.getText())) {
                    confirmPass.setErrorEnabled(false);
                    return true;
                } else {
                    confirmPass.setError(getString(R.string.confirm_password_warning));
                    confirmPass.getFocus(getBaseContext());
                    return false;
                }
            } else {
                newPass.setError(getString(R.string.password_invalid));
                newPass.getFocus(getBaseContext());
                return false;
            }
        } else {
            oldPass.setError(getString(R.string.password_invalid));
            oldPass.getFocus(getBaseContext());
            return false;
        }
    }

    private void showErrorDialog(String message) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(AccountSecurity.this)
                .setTitle("Oops!")
                .setIcon(R.drawable.icon_error)
                .setMessage(message)
                .setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.SECURITY;
    }
}
