package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.interfaces.User;
import com.rohitsuratekar.NCBSinfo.utilities.General;

public class Home extends AppCompatActivity implements User {

    public final String TAG = getClass().getSimpleName();

    ImageView homeIcon;
    ImageView fragment1, fragment2, fragment3;
    SharedPreferences pref;
    DisplayMetrics metrics;
    int f1_x, f1_y, f2_x, f2_y, f3_x, f3_y;
    Button online, offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        homeIcon = (ImageView) findViewById(R.id.home_icon);
        fragment1 = (ImageView) findViewById(R.id.home_fragment1);
        fragment2 = (ImageView) findViewById(R.id.home_fragment2);
        fragment3 = (ImageView) findViewById(R.id.home_fragment3);


        //Initialize app with latest app version
        try {
            pref.edit().putInt(APP_VERSION, getPackageManager().getPackageInfo(getPackageName(), 0).versionCode).apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);


        online = (Button) findViewById(R.id.home_onlineBtn);
        offline = (Button) findViewById(R.id.home_offlineBtn);


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putString(MODE, ONLINE).apply();
                startActivity(new Intent(Home.this, Login.class));
            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putString(MODE, OFFLINE).apply();
                startActivity(new Intent(Home.this, OfflineHome.class));
            }
        });


        f1_x = 0;
        f1_y = 0;
        f2_x = 0;
        f2_y = 0;
        f3_x = 0;
        f3_y = 0;

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragments();
            }
        });

        setFragments();
        runnable.run();

    }

    private void setFragments() {
        int temp1 = getX();
        int temp2 = getY();
        int temp3 = getX();
        int temp4 = getY();
        int temp5 = getX();
        int temp6 = getY();
        Translate(fragment1, f1_x, f1_y, temp1, temp2);
        f1_x = temp1;
        f1_y = temp2;
        Translate(fragment2, f2_x, f2_y, temp3, temp4);
        f2_x = temp3;
        f2_y = temp4;
        Translate(fragment3, f3_x, f3_y, temp5, temp6);
        f3_x = temp5;
        f3_y = temp6;


    }

    private int getX() {
        ViewGroup.LayoutParams params = fragment1.getLayoutParams();
        return new General().randInt(params.width, metrics.widthPixels - params.width);
    }

    private int getY() {
        ViewGroup.LayoutParams params = fragment1.getLayoutParams();
        return new General().randInt(params.height, metrics.heightPixels / 2 - params.height);
    }

    private void Translate(ImageView view, int x, int y, int newX, int newY) {

        TranslateAnimation anim = new TranslateAnimation(x, newX, y, newY);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {

            setFragments();

            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Home timer stopped");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "Home timer stopped");
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        runnable.run();
        super.onRestart();
    }
}
