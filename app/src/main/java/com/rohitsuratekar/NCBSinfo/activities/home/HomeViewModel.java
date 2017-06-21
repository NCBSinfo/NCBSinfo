package com.rohitsuratekar.NCBSinfo.activities.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.activities.transport.NextTrip;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class HomeViewModel extends AndroidViewModel implements OnFinishLoading {

    private MutableLiveData<List<HomeCardModel>> cardList = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);
        new PrepareHome(this, application).execute();
    }

    @Override
    public void onFinish(List<HomeCardModel> cardModels) {
        cardList.postValue(cardModels);
    }

    MutableLiveData<List<HomeCardModel>> getCardList() {
        return cardList;
    }

    private static class PrepareHome extends AsyncTask<Void, Void, Void> {

        private OnFinishLoading loading;
        private AppData db;
        private List<HomeCardModel> modelList;

        PrepareHome(OnFinishLoading loading, Context context) {
            this.loading = loading;
            this.db = AppData.getDatabase(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            modelList = new ArrayList<>();
            RouteData favRoute = null;
            boolean breakDefault = true;
            List<RouteData> dataList = db.routes().getRouteNames();
            for (RouteData r : dataList) {
                if (r.isFavorite() && breakDefault) {
                    favRoute = r;
                    breakDefault = false;
                } else {
                    List<TripData> tripData = db.trips().getTripsByRoute(r.getRouteID());
                    String[] nextTrip;
                    try {
                        nextTrip = new NextTrip(tripData).calculate(Calendar.getInstance());
                    } catch (ParseException e) {
                        Log.error(e.getMessage());
                        nextTrip = new String[]{"00:00", "0"};
                    }
                    modelList.add(new HomeCardModel(r, nextTrip));
                }
            }
            //Put favorite on the top of the list
            if (favRoute != null) {
                List<TripData> tripData = db.trips().getTripsByRoute(favRoute.getRouteID());
                String[] nextTrip;
                try {
                    nextTrip = new NextTrip(tripData).calculate(Calendar.getInstance());
                } catch (ParseException e) {
                    Log.error(e.getMessage());
                    nextTrip = new String[]{"00:00", "0"};
                }
                modelList.add(0, new HomeCardModel(favRoute, nextTrip));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.onFinish(modelList);
        }
    }
}
