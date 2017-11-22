package com.rohitsuratekar.NCBSinfo.activities.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.rohitsuratekar.NCBSinfo.background.SetUpHome;

/**
 * Created by Rohit Suratekar on 21-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class HomeViewModel extends ViewModel implements SetUpHome.OnLoad {

    private MutableLiveData<HomeObject> homeObject = new MutableLiveData<>();
    private MutableLiveData<Boolean> createDefault = new MutableLiveData<>();

    public void startCalculations(Context context, boolean adjustFav) {
        new SetUpHome(context, adjustFav, this).execute();
    }

    public MutableLiveData<HomeObject> getHomeObject() {
        return homeObject;
    }

    public MutableLiveData<Boolean> getCreateDefault() {
        return createDefault;
    }

    @Override
    public void loaded(HomeObject obj) {
        if (obj != null) {
            homeObject.postValue(obj);
        } else {
            createDefault.postValue(true);
        }
    }
}
