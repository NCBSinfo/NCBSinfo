package com.rohitsuratekar.NCBSinfo.background;

import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.Route;
import com.rohitsuratekar.NCBSinfo.database.Trips;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class LoadData extends AsyncTask<Void, Void, Void> {

    private final Database mDb;
    private List<RouteInfo> infoList;
    private OnDataLoad dataLoad;

    LoadData(Context context, OnDataLoad dataLoad) {
        mDb = Database.getDatabase(context);
        this.dataLoad = dataLoad;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        infoList = new ArrayList<>();
        List<Route> routeList = mDb.routes().loadRoadNames();
        for (Route r : routeList) {
            List<Trips> trips = mDb.trips().getTrips(r.getRouteID());
            if (trips.size() > 0) {
                infoList.add(new RouteInfo(r, trips));
            } else {
                Log.inform("Skipping " + r.getOrigin() + " " + r.getDestination());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dataLoad.loaded(infoList);
    }
}
