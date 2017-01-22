package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

/**
 * Background task to get Firebase Auth Token
 */
public class GetToken extends AsyncTask<Object, Void, Void> {

    private onTokenRetrieval master;

    public GetToken(onTokenRetrieval master) {
        this.master = master;
    }

    @Override
    protected Void doInBackground(Object... params) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().getToken(false).addOnCompleteListener(
                    new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                master.token(true, task.getResult().getToken());
                            } else {
                                master.token(false, task.getException().getLocalizedMessage());
                            }
                        }
                    }
            );
        } else {
            master.token(false, "No User Found");
        }
        return null;
    }

    public interface onTokenRetrieval {
        void token(boolean isTokenRetrieved, String token);
    }
}
