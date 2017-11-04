package com.rohitsuratekar.NCBSinfo.activities.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.rohitsuratekar.NCBSinfo.common.AppPrefs;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

// Keep Public Class
public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> userDataReceived = new MutableLiveData<>();

    public void startLoadingProcess(Context context) {
        AppPrefs prefs = new AppPrefs(context);
    }

    public MutableLiveData<Boolean> getUserDataReceived() {
        return userDataReceived;
    }
}
