package com.rohitsuratekar.NCBSinfo.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.home.HomeObject;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 13-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class SetUpHome extends AsyncTask<Void, Void, Void> {

    private OnLoad onLoad;
    private AppData db;
    private HomeObject object;
    private int favorite = -1;
    private boolean adjustFavorite;
    private AppPrefs prefs;

    public SetUpHome(Context context, boolean adjustFavorite, OnLoad onLoad) {
        this.adjustFavorite = adjustFavorite;
        this.onLoad = onLoad;
        this.db = AppData.getDatabase(context);
        this.prefs = new AppPrefs(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (adjustFavorite) {
            int favRoute = db.routes().getRouteNo(prefs.getFavoriteOrigin(), prefs.getFavoriteDestination(), prefs.getFavoriteType());
            if (favRoute != 0) {
                db.routes().removeAllFavorite();
                db.routes().setFavorite(favRoute);
            }
        }
        List<RouteData> routeData = db.routes().getRouteNames();
        SparseArray<RouteData> routes = new SparseArray<>();
        SparseArray<List<TripData>> trips = new SparseArray<>();
        for (RouteData r : routeData) {
            routes.put(r.getRouteID(), r);
            trips.put(r.getRouteID(), db.trips().getTripsByRoute(r.getRouteID()));
            if (r.getFavorite().equals("yes")) {
                this.favorite = r.getRouteID();
            }
        }
        if (routes.size() > 0) {
            this.object = new HomeObject(routes, trips, this.favorite);
        } else {
            this.object = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.onLoad.loaded(this.object);
    }

    public interface OnLoad {
        void loaded(HomeObject homeObject);
    }
}
