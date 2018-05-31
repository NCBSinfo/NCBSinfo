package com.rohitsuratekar.NCBSinfo.fragments.settings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.common.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.common.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.common.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 03-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */
// Keep constructor public
public class SettingsViewModel extends ViewModel {

    private MutableLiveData<List<RouteData>> allRoutes = new MutableLiveData<>();
    private MutableLiveData<Boolean> resetDone = new MutableLiveData<>();

    void loadRoute(final Context context) {
        new GetAllRoutes(context, new OnFinish() {
            @Override
            public void finished() {

            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {
                allRoutes.postValue(routeDataList);
            }
        }).execute();
    }

    void startRest(final Context context) {

        new ResetRoutes(new OnFinish() {
            @Override
            public void finished() {
                resetDone.postValue(true);
            }

            @Override
            public void allRoutes(List<RouteData> routeDataList) {

            }
        }).execute(context);

    }

    MutableLiveData<List<RouteData>> getAllRoutes() {
        return allRoutes;
    }

    public MutableLiveData<Boolean> getResetDone() {
        return resetDone;
    }

    private static class ResetRoutes extends AsyncTask<Object, Void, Void> {

        private OnFinish onFinish;


        public ResetRoutes(OnFinish onFinish) {
            this.onFinish = onFinish;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            Context contexts = (Context) objects[0];
            AppData db = AppData.getDatabase(contexts);
            db.routes().deleteAll();
            db.trips().deleteAll();
            Log.i("Settings", "All Routes deleted");
            new CreateDefaultRoutes(contexts, new OnFinish() {
                @Override
                public void finished() {
                    onFinish.finished();
                }

                @Override
                public void allRoutes(List<RouteData> routeDataList) {

                }
            }).execute();
            return null;
        }
    }


}
