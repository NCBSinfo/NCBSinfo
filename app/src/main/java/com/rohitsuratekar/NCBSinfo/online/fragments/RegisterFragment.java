package com.rohitsuratekar.NCBSinfo.online.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class RegisterFragment extends Fragment {

    //Public
    public static final String REGISTERED = "registeredUser";
    public static final String USERNAME = "currentUsername";
    public static final String EMAIL = "currentEmail";

    //Local
    private static String TAG = "RegisterFragment";

    TextInputEditText username, email, password;
    TextInputLayout user_layout, email_layout, password_layout;
    View rootView;
    private ProgressDialog progress ;
    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.register, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button registerBtn = (Button)rootView.findViewById(R.id.button_register);
        username = (TextInputEditText)rootView.findViewById(R.id.edittext_register_name);
        password = (TextInputEditText)rootView.findViewById(R.id.edittext_register_password);
        email = (TextInputEditText)rootView.findViewById(R.id.edittext_register_email);
        user_layout = (TextInputLayout)rootView.findViewById(R.id.input_layout_register_name);
        password_layout = (TextInputLayout)rootView.findViewById(R.id.input_layout_register_pass);
        email_layout = (TextInputLayout)rootView.findViewById(R.id.input_layout_register_email);
        progress = new ProgressDialog(getContext());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePass() && validateUser()) {
                    progress.setMessage("Registering ...");
                    progress.show();
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        progress.dismiss();
                                        try {
                                            Log.i(TAG,task.getResult().toString());
                                        }
                                        catch (RuntimeExecutionException e){
                                            if(e.getMessage().contains("FirebaseAuthUserCollisionException")){
                                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                                alertDialog.setTitle("User exists");
                                                alertDialog.setMessage("Email "+ mAuth.getCurrentUser().getEmail()+ " is already used by another user. If you think it was you, click on reset below and you will receive email with further instructions.");
                                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                                                        alertDialog.dismiss();
                                                    }
                                                });
                                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Go Back", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        alertDialog.dismiss();
                                                    }
                                                });

                                                alertDialog.show();
                                            }
                                            else if (e.getMessage().contains("FirebaseAuthInvalidCredentialsException")){
                                                email_layout.setError(e.getCause().getMessage());
                                                requestFocus(email);
                                            }
                                            else{
                                                Log.i(TAG,e.getMessage());
                                            }
                                        }

                                    }
                                    else {
                                        progress.setMessage("Signing in...");
                                        pref.edit().putString(USERNAME, username.getText().toString()).apply();
                                        pref.edit().putString(EMAIL, email.getText().toString()).apply();
                                        pref.edit().putBoolean(REGISTERED, true).apply();
                                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progress.dismiss();
                                                if (!task.isSuccessful()) {
                                                    Log.w(TAG, "signInWithEmail", task.getException());
                                                    if (task.getException().toString().contains("FirebaseAuthInvalidUserException")) {
                                                        user_layout.setError("Email is not found in our database, try registering instead ?");
                                                        requestFocus(username);
                                                    }
                                                    if (task.getException().toString().contains("FirebaseAuthInvalidCredentialsException")) {
                                                        password_layout.setError("Wrong Password");
                                                        requestFocus(password);
                                                    }
                                                } else {


                                                    startActivity(new Intent(getActivity(), OnlineHome.class));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        Button gotoSignin = (Button)rootView.findViewById(R.id.button_goto_signin);
        gotoSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment, new SignInFragment())
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        return rootView;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePass() {
        if (password.getText().toString().trim().isEmpty()) {
            password_layout.setError("Password can not be empty");
            requestFocus(password);
            return false;
        }
        else if (password.length()<6){
            password_layout.setError("Password must be at least 6 character long");
            requestFocus(password);
            return false;
        }
        else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUser() {
        if (username.getText().toString().trim().isEmpty()) {
            user_layout.setError("Name can not be empty");
            requestFocus(username);
            return false;
        }
        else {
            user_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {

        if ((email.getText().toString().trim().isEmpty()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){
            email_layout.setError("Invalid Email");
            requestFocus(email);
            return false;
        }
        else {
            email_layout.setErrorEnabled(false);
        }
        return true ;
    }
}