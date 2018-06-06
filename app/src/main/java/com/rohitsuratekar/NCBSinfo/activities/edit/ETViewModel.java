package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.common.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private MutableLiveData<ETDataHolder> data = new MutableLiveData<>();
    private MutableLiveData<List<RouteData>> routeList = new MutableLiveData<>();
    private MutableLiveData<Boolean> editSetup = new MutableLiveData<>();
    private MutableLiveData<Boolean> finalData = new MutableLiveData<>();
    private MutableLiveData<ConfirmModel> confirmActions = new MutableLiveData<>();

    public ETViewModel(Application application) {
        super(application);
        data.postValue(new ETDataHolder());
        currentStep.postValue(0);
        new GetAllRoutes(application.getApplicationContext(), new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                routeList.postValue(routeDataList);
            }
        }).execute();
    }

    void editActivityTask(Context context, String[] details) {
        new PrepareETData(new OnDataFinish() {
            @Override
            public void finished(ETDataHolder holder) {
                data.postValue(holder);
                currentStep.postValue(0);
                editSetup.postValue(true);

            }
        }, context).execute(details);
    }

    MutableLiveData<Boolean> getEditSetup() {
        return editSetup;
    }

    MutableLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    MutableLiveData<ETDataHolder> getData() {
        return data;
    }

    MutableLiveData<List<RouteData>> getRouteList() {
        return routeList;
    }

    static class PrepareETData extends AsyncTask<String, Void, Void> {

        private OnDataFinish finish;
        private AppData db;

        PrepareETData(OnDataFinish finish, Context context) {
            this.finish = finish;
            db = AppData.getDatabase(context);
        }


        @Override
        protected Void doInBackground(String... objects) {
            if (objects[0] != null && objects[1] != null && objects[2] != null) {
                String[] details = new String[]{objects[0], objects[1], objects[2]};
                int routeNo = db.routes().getRouteNo(details[0], details[1], details[2]);
                RouteData routeData = db.routes().getRoute(routeNo);
                List<TripData> tripData = db.trips().getTripsByRoute(routeNo);
                ETDataHolder dataHolder = new ETDataHolder();
                dataHolder.setDestination(routeData.getDestination().toUpperCase());
                dataHolder.setOrigin(routeData.getOrigin().toUpperCase());
                dataHolder.setType(getTransportType(routeData.getType()));
                dataHolder.setTripData(tripData);
                finish.finished(dataHolder);
            }
            return null;
        }
    }

    interface OnDataFinish {
        void finished(ETDataHolder holder);
    }

    private static int getTransportType(String type) {
        switch (type.toLowerCase().trim()) {
            case "shuttle":
                return 0;
            case "ttc":
                return 1;
            case "buggy":
                return 2;
            default:
                return 3;
        }
    }

    public void validateInfo(final Context context, ConfirmModel model) {

        new ValidateData(context, model, new OnValidate() {
            @Override
            public void getModel(ConfirmModel model) {
                if (model.getActionCode() == ConfirmModel.ACTION_DAY_CONFLICT) {
                    model.setErrorMessage(R.string.et_conflict_day);
                    model.setConfirmed(false);
                    confirmActions.postValue(model);
                } else if (model.getActionCode() == ConfirmModel.ACTION_NO_CONFLICT) {
                    model.setErrorMessage(R.string.et_no_conflict_day);
                    model.setConfirmed(false);
                    confirmActions.postValue(model);
                } else if (model.getActionCode() == ConfirmModel.ACTION_ROUTE_CONFLICT) {
                    model.setErrorMessage(R.string.et_route_conflict);
                    model.setConfirmed(false);
                    confirmActions.postValue(model);
                } else if (model.getActionCode() == ConfirmModel.ACTION_DONE) {
                    model.setConfirmed(true);
                    confirmActions.postValue(model);
                }
            }
        }).execute();

    }

    public MutableLiveData<Boolean> getFinalData() {
        return finalData;
    }

    public void setFinalData(Boolean dataHolder) {
        finalData.postValue(dataHolder);
    }

    public MutableLiveData<ConfirmModel> getConfirmActions() {
        return confirmActions;
    }

    static class ValidateData extends AsyncTask<Void, Void, Void> {

        private ConfirmModel model;
        private OnValidate validate;
        private AppData db;

        ValidateData(Context context, ConfirmModel model, OnValidate validate) {
            this.model = model;
            this.validate = validate;
            this.db = AppData.getDatabase(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (model.isConfirmed()) {
                int routeNo = db.routes().getRouteNo(model.getData().getOrigin().toLowerCase(), model.getData().getDestination().toLowerCase(), EditTransport.convertType(model.getData().getType()));
                for (int i = 0; i < model.getData().getFrequencyDetails().length; i++) {
                    if (model.getData().getFrequencyDetails()[i] == 1) {
                        int tripID = db.trips().getTripID(routeNo, i + 1);
                        TripData data = new TripData();
                        data.setRouteID(routeNo);
                        data.setDay(i + 1);
                        data.setTrips(model.getData().getItemList());
                        if (tripID == 0) {
                            db.trips().insertTrips(data);
                        } else {
                            data.setTripID(tripID);
                            db.trips().updateTrips(data);
                        }
                    }
                }
                model.setActionCode(ConfirmModel.ACTION_DONE);
                validate.getModel(model);
            } else {
                int routeNo = db.routes().getRouteNo(model.getData().getOrigin().toLowerCase(), model.getData().getDestination().toLowerCase(), EditTransport.convertType(model.getData().getType()));
                if (routeNo != 0) {
                    if (model.isForEdit()) {
                        List<TripData> tripData = db.trips().getTripsByRoute(routeNo);
                        for (TripData d : tripData) {
                            if (model.getData().getFrequencyDetails()[d.getDay() - 1] == 1) {
                                model.setActionCode(ConfirmModel.ACTION_DAY_CONFLICT);
                                validate.getModel(model);
                                break;
                            }
                        }
                        if (model.getActionCode() == 0) {
                            model.setActionCode(ConfirmModel.ACTION_NO_CONFLICT);
                            validate.getModel(model);
                        }
                    } else {
                        model.setActionCode(ConfirmModel.ACTION_ROUTE_CONFLICT);
                        validate.getModel(model);
                    }
                } else {
                    Log.i("EditTransportViewModel", "New route will be created");
                    RouteData routeData = new RouteData();
                    routeData.setOrigin(model.getData().getOrigin().toLowerCase());
                    routeData.setDestination(model.getData().getDestination().toLowerCase());
                    routeData.setType(EditTransport.convertType(model.getData().getType()));
                    routeData.setAuthor("NCBSinfo");
                    routeData.setCreatedOn(Helper.timestamp());
                    routeData.setModifiedOn(Helper.timestamp());
                    routeData.setFavorite("no");

                    int routeID = (int) db.routes().insertRoute(routeData);
                    for (int i = 0; i < model.getData().getFrequencyDetails().length; i++) {
                        if (model.getData().getFrequencyDetails()[i] == 1) {
                            TripData data = new TripData();
                            data.setRouteID(routeID);
                            data.setDay(i + 1);
                            data.setTrips(model.getData().getItemList());
                            db.trips().insertTrips(data);
                        }
                    }

                    model.setActionCode(ConfirmModel.ACTION_DONE);
                    validate.getModel(model);
                }
            }
            return null;
        }
    }

    interface OnValidate {
        void getModel(ConfirmModel model);
    }
}
