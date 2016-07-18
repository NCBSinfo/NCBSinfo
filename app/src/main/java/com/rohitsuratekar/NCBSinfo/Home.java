package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.activities.login.Registration;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements AppConstants {

    public final String TAG = getClass().getSimpleName();

    //Set UI
    @BindView(R.id.home_icon)
    ImageView homeIcon;
    @BindView(R.id.home_fragment1)
    ImageView fragment1;
    @BindView(R.id.home_fragment2)
    ImageView fragment2;
    @BindView(R.id.home_fragment3)
    ImageView fragment3;

    DisplayMetrics metrics;
    int f1_x, f1_y, f2_x, f2_y, f3_x, f3_y;
    Button online, offline;
    Preferences pref;
    BaseParameters baseParameters;

<<<<<<< HEAD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        //Initialization
        pref = new Preferences(getBaseContext());
        baseParameters = new BaseParameters(getBaseContext());


        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            pref.app().setAppVersionName(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref.app().addOpenCount(); //Whenever user opens app
=======
import com.rohitsuratekar.NCBSinfo.background.Alarms;
import com.rohitsuratekar.NCBSinfo.common.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.offline.OfflineHome;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.login.Registration;
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b

        startActivity(new Intent(this, OnlineHome.class));
        new Preferences(getBaseContext()).user().setUserType(userType.REGULAR_USER);
        new Preferences(getBaseContext()).app().setMode(modes.ONLINE);


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);


        online = (Button) findViewById(R.id.home_onlineBtn);
        offline = (Button) findViewById(R.id.home_offlineBtn);


<<<<<<< HEAD
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pref.app().setMode(modes.OFFLINE);
                startActivity(new Intent(Home.this, OfflineHome.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
=======
            case registration.camp16.CAMP_MODE:
                pref.edit().putString(MODE, ONLINE).apply();
                startActivity(new Intent(Home.this, OnlineHome.class));
                overridePendingTransition(0, 0);
                break;
        }
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b

            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Registration.class);
                startActivity(intent);
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
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
<<<<<<< HEAD
            public void onClick(View view) {
                setFragments();
=======
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_offline));
                alertDialog.setMessage(getResources().getString(R.string.warning_offline_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pref.edit().putString(Home.MODE, Home.OFFLINE).apply();
                        setTransportValue(); //Reset transport values
                        alertDialog.dismiss();
                        Intent intent = new Intent(Home.this, OfflineHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
>>>>>>> c62419471cbfa3ab9aec5ba321ef4effdd13a64b
            }
        });

        setFragments();
        runnable.run();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


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
