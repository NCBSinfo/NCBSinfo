package com.rohitsuratekar.NCBSinfo.online.fragments;

import android.app.ProgressDialog;
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
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;

public class SignInFragment extends Fragment {

    //Local
    private static String TAG = "SignInFragment";
    private ProgressDialog progress;
    private DatabaseReference mDatabase;

    TextInputEditText username, password;
    TextInputLayout user_layout, password_layout;
    View rootView;
    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sign_in, container, false);

        //Initialization
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progress = new ProgressDialog(getContext());

        //UI
        Button signInButton = (Button) rootView.findViewById(R.id.button_sign_in);
        username = (TextInputEditText) rootView.findViewById(R.id.edittext_signin_username);
        password = (TextInputEditText) rootView.findViewById(R.id.edittext_signin_password);
        user_layout = (TextInputLayout) rootView.findViewById(R.id.input_layout_signin_user);
        password_layout = (TextInputLayout) rootView.findViewById(R.id.input_layout_signin_pass);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePass()) {
                    progress.setMessage("Signing in ...");
                    progress.show();
                    mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progress.dismiss();
                                Log.w(TAG, "signInWithEmail", task.getException());
                                if (task.getException().toString().contains("FirebaseAuthInvalidUserException")) {
                                    user_layout.setError(getString(R.string.warning_registration_no_email_found));
                                    requestFocus(username);
                                }
                                if (task.getException().toString().contains("FirebaseAuthInvalidCredentialsException")) {
                                    password_layout.setError(getString(R.string.warning_registration_wrong_password));
                                    requestFocus(password);
                                }
                            } else {
                                progress.setMessage("Updating data...");
                                //Retrieve data from database here
                                mDatabase.child(RemoteConstants.USER_NODE).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    switch (child.getKey()) {
                                                        case RemoteConstants.USERNAME:
                                                            pref.edit().putString(RegisterFragment.USERNAME, child.getValue().toString()).apply();
                                                            break;
                                                        case RemoteConstants.EMAIL:
                                                            pref.edit().putString(RegisterFragment.EMAIL, child.getValue().toString()).apply();
                                                            break;
                                                        case RegisterFragment.RESEARCH_TALK:
                                                            pref.edit().putInt(RegisterFragment.RESEARCH_TALK, Integer.parseInt(child.getValue().toString())).apply();
                                                            break;
                                                        default:
                                                            Log.i(TAG, "Unknown Key :" + child.getKey());
                                                    }
                                                }
                                                //Always keep registration true after sign in
                                                pref.edit().putBoolean(RegisterFragment.REGISTERED, true).apply();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                            }
                                        });
                                progress.dismiss();
                                pref.edit().putString(Home.MODE, Home.ONLINE).apply();
                                startActivity(new Intent(getActivity(), OnlineHome.class));
                            }
                        }
                    });
                }
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
