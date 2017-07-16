package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private MutableLiveData<ETDataHolder> data = new MutableLiveData<>();

    public ETViewModel(Application application) {
        super(application);
        data.postValue(new ETDataHolder());
        currentStep.postValue(0);
    }

    public MutableLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    public MutableLiveData<ETDataHolder> getData() {
        return data;
    }
}
