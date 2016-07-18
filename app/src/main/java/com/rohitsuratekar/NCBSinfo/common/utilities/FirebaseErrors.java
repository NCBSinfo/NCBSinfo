package com.rohitsuratekar.NCBSinfo.common.utilities;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.rohitsuratekar.NCBSinfo.R;

public class FirebaseErrors {

    public static final String INVALID_USER = "invalid_user";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String TOO_MANY_ATTEMPTS = "too_many_attempts";
    public static final String USER_EXISTS = "user_exists";


    String exception;
    Context context;
    String warningMessage;
    String type;
    Exception e;
    Activity activity;

    public FirebaseErrors(Context context, Exception e, Activity activity) {
        this.context = context;
        this.exception = e.toString();
        this.e = e;
        this.activity = activity;
        CheckException();

    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public String getType() {
        return type;
    }

    private void CheckException(){

        if(exception.contains("FirebaseAuthInvalidUserException")){
            this.warningMessage = context.getString(R.string.warning_firebase_user_does_not_exist);
            this.type = INVALID_USER;
        }
        else if(exception.contains("FirebaseAuthInvalidCredentialsException")){
            this.warningMessage = e.getLocalizedMessage();
            this.type = WRONG_PASSWORD;
        }
        else if (exception.contains("FirebaseTooManyRequestsException")){
            this.warningMessage = context.getString(R.string.warning_firebase_too_many_attemps);
            this.type = TOO_MANY_ATTEMPTS;
        }
        else if(exception.contains("FirebaseAuthUserCollisionException")){
            this.warningMessage = context.getString(R.string.warning_user_exists);
            this.type = USER_EXISTS;
        }
        else {
            this.warningMessage = e.getLocalizedMessage();
            this.type = "unknown";
        }
    }

    public void dialogWarning(){
        new AlertDialog.Builder(activity)
                .setTitle("Oops!")
                .setMessage(warningMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.icon_sadface)
                .show();
    }
}
