package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public class LoginSessionObject {

    private Context context;
    private FirebaseAuth mAuth;
    private String email;
    private String password;

    public LoginSessionObject(Context context, FirebaseAuth mAuth, String email, String password) {
        this.context = context;
        this.mAuth = mAuth;
        this.email = email;
        this.password = password;
    }

    public LoginSessionObject() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
