package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 21-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class EditTransportViewModel extends ViewModel {

    private MutableLiveData<List<StepperModel>> stepperList = new MutableLiveData<>();

    private MutableLiveData<RouteInfoModel> routeModel = new MutableLiveData<>();


    void initializeProcess() {
        List<StepperModel> modelList = new ArrayList<>();
        modelList.add(new StepperModel(R.string.add_route_basic, R.string.add_route_basic_note, StepperModel.STATE_SELECTED, true));
        modelList.add(new StepperModel(R.string.add_route_trips, R.string.add_route_trip_note, StepperModel.STATE_INITIATED));
        modelList.add(new StepperModel(R.string.add_route_confirm, R.string.add_route_confirm_note, StepperModel.STATE_INITIATED));
        stepperList.postValue(modelList);
        routeModel.postValue(new RouteInfoModel());

    }

    MutableLiveData<List<StepperModel>> getStepperList() {
        return stepperList;
    }

    MutableLiveData<RouteInfoModel> getRouteModel() {
        return routeModel;
    }
}
