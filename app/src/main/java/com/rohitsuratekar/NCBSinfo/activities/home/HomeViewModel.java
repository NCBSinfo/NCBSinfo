package com.rohitsuratekar.NCBSinfo.activities.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.SetUpHome;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.rohitsuratekar.NCBSinfo.common.Helper.convertToList;

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

    public void updateNCBS_ICTS(final Context context) {
        new updateRoute(context, new routeUpdated() {
            @Override
            public void updated() {
                setHome(context);
            }
        }).execute();
    }

    private void setHome(Context context) {
        new SetUpHome(context, false, this).execute();
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

    static class updateRoute extends AsyncTask<Void, Void, Void> {

        private routeUpdated routeUpdated;
        private AppData appData;
        private List<String> ncbsToICTS_1 = new ArrayList<>();
        private List<String> ncbsToICTS_2 = new ArrayList<>();
        private List<String> ncbsToICTS_sunday = new ArrayList<>();

        updateRoute(Context context, HomeViewModel.routeUpdated routeUpdated) {
            this.routeUpdated = routeUpdated;
            this.appData = AppData.getDatabase(context);
            ncbsToICTS_1 = convertToList(context.getString(R.string.def_ncbs_icts_week_1));
            ncbsToICTS_2 = convertToList(context.getString(R.string.def_ncbs_icts_week_2));
            ncbsToICTS_sunday = convertToList(context.getString(R.string.def_ncbs_icts_sunday));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int routeNo = appData.routes().getRouteNo("ncbs", "icts", "shuttle");
            if (routeNo != 0) {
                appData.trips().deleteTripsByRoute(routeNo);
                Log.inform("Old NCBS-ICTS route deleted");
                for (int day = Calendar.SUNDAY; day < Calendar.SATURDAY + 1; day++) {
                    TripData t4 = new TripData();
                    t4.setRouteID(routeNo);
                    t4.setDay(day);
                    switch (day) {
                        case Calendar.SUNDAY:
                            t4.setTrips(ncbsToICTS_sunday);
                            break;
                        case Calendar.MONDAY:
                        case Calendar.WEDNESDAY:
                        case Calendar.THURSDAY:
                            t4.setTrips(ncbsToICTS_1);
                            break;
                        default:
                            t4.setTrips(ncbsToICTS_2);
                    }
                    appData.trips().insertTrips(t4);
                }
                Log.inform("new NCBS-ICTS route created");
                RouteData rd = appData.routes().getRoute(routeNo);
                rd.setAuthor("NCBSinfo");
                rd.setModifiedOn(General.timeStamp());
                appData.routes().updateRoute(rd);
                routeUpdated.updated();
            } else {
                routeUpdated.updated();
            }
            return null;
        }


    }

    interface routeUpdated {
        void updated();
    }
}
