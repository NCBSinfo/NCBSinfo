package com.rohitsuratekar.NCBSinfo.background;

import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class GetAllRoutes extends AsyncTask<Void, Void, Void> {

    private OnFinish finish;
    private AppData db;
    private List<RouteData> routeData;

    public GetAllRoutes(Context context, OnFinish finish) {
        this.finish = finish;
        this.db = AppData.getDatabase(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        routeData = db.routes().getRouteNames();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        finish.allRoutes(routeData);
    }
}
