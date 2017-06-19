package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.Route;
import com.rohitsuratekar.NCBSinfo.database.Trips;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startActivity(new Intent(this, Home.class));

        //new Test().execute();

    }

    class Test extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Database database = Database.getDatabase(getApplicationContext());

            Route route = new Route();
            route.setOrigin("ncbs");
            route.setDestination("mandara");
            route.setType("buggy");
           // database.routes().insertRoute(route);
            List<String> strings = new ArrayList<>();
            strings.add("07:00");
            strings.add("10:30");
            strings.add("16:00");
            strings.add("20:00");
            strings.add("00:00");
            Trips trips = new Trips();
            trips.setTrips(strings);
            trips.setDay(2);
            trips.setRouteID(database.routes().getRouteNo(route.getOrigin(), route.getDestination(), route.getType()));
         //   database.trips().insertTrips(trips);
            Log.inform("Added");

            for (Route r : database.routes().loadRoadNames()) {
                Log.inform(r.getOrigin(), r.getDestination());
            }

            for (Trips t : database.trips().getTrips(trips.getRouteID())) {
                Log.inform(t.getTrips());
            }

            return null;
        }
    }


}
