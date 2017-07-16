package com.rohitsuratekar.NCBSinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.background.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.Log;

import java.util.List;

public class Splash extends AppCompatActivity implements OnFinish {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        progressDialog = new ProgressDialog(Splash.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.hm_loading));
        progressDialog.show();
        new GetAllRoutes(getBaseContext(), this).execute();
    }

    @Override
    public void finished() {
        gotoHome();
    }

    @Override
    public void allRoutes(List<RouteData> routeDataList) {
        //Keep blank
        if (routeDataList.size() != 0) {
            gotoHome();
        } else {
            Log.inform("No routes found. Creating default ones.");
            new CreateDefaultRoutes(this, getBaseContext()).execute();
        }
    }

    private void gotoHome() {
        progressDialog.dismiss();
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
