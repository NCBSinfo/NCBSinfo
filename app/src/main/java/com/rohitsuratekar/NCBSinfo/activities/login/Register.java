package com.rohitsuratekar.NCBSinfo.activities.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoginSessionObject;
import com.rohitsuratekar.NCBSinfo.background.tasks.RegisterUser;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.views.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends BaseActivity {

    @BindView(R.id.reg_in_name)
    InputView name;
    @BindView(R.id.reg_in_email)
    InputView email;
    @BindView(R.id.reg_in_pass)
    InputView pass;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setCancelable(false);

        //Need following line to remove custom text instance
        name.getEditText().setSaveEnabled(false);
        email.getEditText().setSaveEnabled(false);
        pass.getEditText().setSaveEnabled(false);

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

        name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (name.getText().length() > 1) {
                    name.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass.getText().length() > 5) {
                    pass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick(R.id.reg_btn_submit)
    public void submitDetails() {
        if (General.isNetworkAvailable(getBaseContext())) {
            if (validate()) {
                confirmPassword();
            }
        } else {
            General.makeLongToast(getBaseContext(), getString(R.string.network_error));
        }

    }

    private void startNetworkCall() {
        progressDialog.show();
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        LoginSessionObject object = new LoginSessionObject();
        object.setContext(getBaseContext());
        object.setEmail(email.getText());
        object.setPassword(pass.getText());
        object.setName(name.getText());
        object.setmAuth(mAuth);

        new RegisterUser(new RegisterUser.OnDataRetrieved() {
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
                Intent intent = new Intent(Register.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                animateTransition();
            }
        }).execute(object);

    }

    private boolean validate() {
        if (General.isValidEmail(email.getText())) {
            email.setErrorEnabled(false);
            if (pass.getText().length() > 5) {
                if (name.isEmpty()) {
                    name.getFocus(getBaseContext());
                    name.setError("Name can not be empty");
                    return false;
                } else {
                    return true;
                }
            } else {
                pass.getFocus(getBaseContext());
                pass.setError(getString(R.string.password_invalid));
                return false;
            }
        } else {
            email.getFocus(getBaseContext());
            email.setError(getString(R.string.email_invalid));
            return false;
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(Register.this)
                .setTitle("Oops!")
                .setIcon(R.drawable.icon_error)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void confirmPassword() {

        AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
        final EditText edittext = new EditText(getBaseContext());
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alert.setMessage("For security reason, reenter your password");
        alert.setTitle(getString(R.string.dashboard_password_details));
        alert.setView(edittext);
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edittext.getText().toString().equals(pass.getText())) {
                    edittext.clearFocus();
                    startNetworkCall();
                } else {
                    General.makeShortToast(getBaseContext(), "Confirm password do not match with password");
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }


    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.REGISTER;
    }
}
