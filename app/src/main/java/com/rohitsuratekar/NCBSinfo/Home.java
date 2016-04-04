package com.rohitsuratekar.NCBSinfo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.activity.Activity_Contact;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Extra;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMModeration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMRegistration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_NotificationReceiver;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Shuttles;
import com.rohitsuratekar.NCBSinfo.constants.DatabaseConstants;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.constants.SettingsConstants;
import com.rohitsuratekar.NCBSinfo.gcm.FlagHandler;
import com.rohitsuratekar.NCBSinfo.helper.helper_shuttles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class Home extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    Animation alphaAnimation, exitAnimation, dropDown;
    int screen_H, isBuggy, changeRoute;
    LinearLayout shuttleSection, shuttleAnimation, shuttleOption, contactSection, contactAnimation, contactOption;
    LinearLayout updatesSection, updatesAnimation, updatesOption, otherSection, otherAnimation, otherOption;
    TextView shuttleName,nextShuttle, timeleft, shuttleNote;
    String transportFROM, transportTO;
    PopupMenu popup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //Delete Old databases
        if(doesDatabaseExist(this,"ContactStore2")){
            this.deleteDatabase("ContactStore2");
        }
        //Check for Flag handlers
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.START_FLAG, false)){
            Intent intent = new Intent(Home.this, FlagHandler.class);
            intent.putExtra(GCMConstants.FLAG_CODE,PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.FLAG_CODE, "0"));
            startActivity(intent);
        }
        //set default values
        changeRoute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SettingsConstants.HOME_DEFAULT_ROUTE,0);
        transportFROM = new helper_shuttles().getRouteName(changeRoute)[0];
        transportTO = new helper_shuttles().getRouteName(changeRoute)[1];
        isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(changeRoute)[2]);

        //Get Height of Device Screen
        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);
        screen_H = matrix.heightPixels;

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.home_section_fade);
        exitAnimation = AnimationUtils.loadAnimation(this,R.anim.home_section_exit);
        dropDown = AnimationUtils.loadAnimation(this,R.anim.drop_down_view);

        //Give warning to users if Android version is lower than 5.0
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(DatabaseConstants.ANDROID_VERSION_WARNING, true)) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("Compatibility mode");
                alertDialog.setMessage("This app is best suited for Android Lollipop (21) and above. Your current android version is " + Build.VERSION.SDK_INT + " . Some animations and functions might not work properly. ");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(DatabaseConstants.ANDROID_VERSION_WARNING, false).apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        }

        //Give warning to bigger screen sizes
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(DatabaseConstants.ANDROID_DEVICE_SIZE_WARNING, true)) {

            if (getResources().getString(R.string.ScreenSize).equals("1")) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("Compatibility mode");
                alertDialog.setMessage("This app not designed for Tablets and Large Screen devices yet. Hence device layouts will be not on scale.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(DatabaseConstants.ANDROID_DEVICE_SIZE_WARNING, false).apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        }

        //App name in middle
        getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar);

        //Search all layouts and assign variables

        shuttleAnimation = (LinearLayout)findViewById(R.id.home_shuttle_animation);
        shuttleSection = (LinearLayout)findViewById(R.id.home_shuttle_Section);
        shuttleOption = (LinearLayout)findViewById(R.id.home_shuttle_options);

        contactAnimation = (LinearLayout)findViewById(R.id.home_contact_animation);
        contactSection = (LinearLayout)findViewById(R.id.home_contact_Section);
        contactOption = (LinearLayout)findViewById(R.id.home_contact_options);

        updatesAnimation = (LinearLayout)findViewById(R.id.home_updates_animation);
        updatesSection = (LinearLayout)findViewById(R.id.home_updates_Section);
        updatesOption = (LinearLayout)findViewById(R.id.home_updates_options);

        otherAnimation = (LinearLayout)findViewById(R.id.home_other_animation);
        otherSection = (LinearLayout)findViewById(R.id.home_other_Section);
        otherOption = (LinearLayout)findViewById(R.id.home_other_options);

        shuttleSection.setOnClickListener(this);
        contactSection.setOnClickListener(this);
        updatesSection.setOnClickListener(this);
        otherSection.setOnClickListener(this);


        SetDefaultView();

        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_fav_layout);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.home_allcontact_layout);
        LinearLayout l3 = (LinearLayout)findViewById(R.id.home_moderator_layout);
        LinearLayout l4 = (LinearLayout)findViewById(R.id.home_register_layout);
        LinearLayout l5 = (LinearLayout)findViewById(R.id.home_log_layout);
        LinearLayout l6 = (LinearLayout)findViewById(R.id.home_otherinfo_layout);
        LinearLayout l7 = (LinearLayout)findViewById(R.id.home_settings_layout);
        LinearLayout l8 = (LinearLayout)findViewById(R.id.home_viewmore_layout);
        LinearLayout l9 = (LinearLayout)findViewById(R.id.home_textHolder);

        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);
        l5.setOnClickListener(this);
        l6.setOnClickListener(this);
        l7.setOnClickListener(this);
        l8.setOnClickListener(this);
        l9.setOnClickListener(this);

        shuttleNote = (TextView)findViewById(R.id.home_shuttle_note);
        timeleft = (TextView)findViewById(R.id.home_shuttle_timeleft);
        nextShuttle = (TextView)findViewById(R.id.home_nextShuttle);
        shuttleName = (TextView)findViewById(R.id.home_shuttleName_text);

        //Timer for timeleft
        Timer timeLeft = new Timer();
        timeLeft.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000); //1000 is milliseconds for each time tick

        changeShuttleText();


    } //OnCreate close

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        changeRoute =0;
        if (item.getItemId() == R.id.route0) {  changeRoute =0;  }
        else if (item.getItemId() == R.id.route1) {changeRoute=1; }
        else if (item.getItemId() == R.id.route2) { changeRoute=2; }
        else if (item.getItemId() == R.id.route3) { changeRoute=3; }
        else if (item.getItemId() == R.id.route4) { changeRoute=4; }
        else if (item.getItemId() == R.id.route5) { changeRoute=5; }
        else if (item.getItemId() == R.id.route6) { changeRoute=6; }
        else if (item.getItemId() == R.id.route7) { changeRoute=7; }
        else if (item.getItemId() == R.id.route8) { changeRoute=8; }

        transportFROM = new helper_shuttles().getRouteName(changeRoute)[0];
        transportTO = new helper_shuttles().getRouteName(changeRoute)[1];
        isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(changeRoute)[2]);
        changeShuttleText();

        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.home_shuttle_Section:
                GeneralAnimationFlow(v, shuttleSection, shuttleAnimation, shuttleOption);
                break;
            case R.id.home_contact_Section:
                GeneralAnimationFlow(v, contactSection, contactAnimation, contactOption);
                break;
            case R.id.home_updates_Section:
                GeneralAnimationFlow(v, updatesSection,updatesAnimation,updatesOption);
                break;
            case R.id.home_other_Section:
                GeneralAnimationFlow(v, otherSection,otherAnimation,otherOption);
                break;
            case R.id.home_fav_layout:
                Intent intent7 = new Intent(Home.this,Activity_Contact.class);
                intent7.putExtra("switch","1");
                startActivity(intent7);
                overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
                break;
            case R.id.home_allcontact_layout:
                Intent intent6 = new Intent(Home.this,Activity_Contact.class);
                intent6.putExtra("switch","0");
                startActivity(intent6);
                overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
                break;
            case R.id.home_moderator_layout:
                Intent intent3 = new Intent(Home.this,Activity_GCMModeration.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.activity_slide_right, R.anim.activity_right_half);
                break;
            case R.id.home_register_layout:
                Intent intent = new Intent(Home.this,Activity_GCMRegistration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_right, R.anim.activity_right_half);
                break;
            case R.id.home_log_layout:
                Intent intent2 = new Intent(Home.this,Activity_NotificationReceiver.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.activity_slide_right, R.anim.activity_right_half);
                break;
            case R.id.home_otherinfo_layout:
                Intent intent4 = new Intent(Home.this,Activity_Extra.class);
                startActivity(intent4);
                overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
                break;
            case R.id.home_settings_layout:
                Intent intent5 = new Intent(Home.this,Settings.class);
                startActivity(intent5);
                overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
                break;
            case R.id.home_viewmore_layout:
                Intent intent8 = new Intent(Home.this, Activity_Shuttles.class);
                intent8.putExtra("switch", String.valueOf(changeRoute));
                startActivity(intent8);
                overridePendingTransition(R.anim.activity_slide_right, R.anim.activity_right_half);
                break;

            case R.id.home_textHolder:
                popup = new PopupMenu(Home.this,shuttleName );
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.home_shuttle_to_fram_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;


            default:
                break;
        }


    }


    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    // Animation to fill section
    public void FillAnimation(final View currentView, final View animatedWindows, final View targetView, int x, int y,int startRadius, int endRadius, boolean flag) {

        Animator anim = null;
        final int id = currentView.getId();

        //Animation will work only of android 21 and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            if (flag) {

                anim = ViewAnimationUtils.createCircularReveal(animatedWindows, x, y, startRadius, endRadius);
                anim.setDuration(500);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        targetView.setVisibility(View.VISIBLE);
                        targetView.startAnimation(alphaAnimation);
                        animatTransport();
                        animateContact();
                        animateUpdate();
                        animateOther();
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        currentView.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                animatedWindows.setVisibility(View.VISIBLE);
                anim.start();
                flag = false;
            }
            else{
                anim = ViewAnimationUtils.createCircularReveal(animatedWindows, x, y, endRadius, startRadius);
                anim.setDuration(400);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        targetView.setVisibility(View.GONE);
                        targetView.startAnimation(exitAnimation);

                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animatedWindows.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                anim.start();
                flag = true;

            }
        }
        //COMPATIBILITY CHECK
        //Change for devices with lower android version
        else{
            if (flag) {
                targetView.setVisibility(View.VISIBLE);
                animatedWindows.setVisibility(View.VISIBLE);
            }
            else{
                targetView.setVisibility(View.GONE);
                animatedWindows.setVisibility(View.GONE);
                flag = true;
            }

        }
    }

    public void GeneralAnimationFlow(View v, View section, View animation, View options ){
        int x,y;

        switch (v.getId()){
            case R.id.home_shuttle_Section:
                x=v.getRight(); y = v.getBottom(); break;
            case R.id.home_contact_Section:
                x=v.getLeft(); y=v.getBottom(); break;
            case R.id.home_updates_Section:
                x=v.getRight(); y=v.getTop(); break;
            case R.id.home_other_Section:
                x=v.getLeft(); y=v.getTop(); break;
            default:
                x=v.getLeft(); y=v.getBottom(); break;

        }
        SetDefaultView();
        FillAnimation(section, animation, options, x, y, 0, screen_H, true);
    }


    public void SetDefaultView (){

        shuttleAnimation.setVisibility(View.GONE);
        shuttleSection.setVisibility(View.VISIBLE);
        shuttleOption.setVisibility(View.GONE);

        contactAnimation.setVisibility(View.GONE);
        contactSection.setVisibility(View.VISIBLE);
        contactOption.setVisibility(View.GONE);

        updatesAnimation.setVisibility(View.GONE);
        updatesSection.setVisibility(View.VISIBLE);
        updatesOption.setVisibility(View.GONE);

        otherAnimation.setVisibility(View.GONE);
        otherSection.setVisibility(View.VISIBLE);
        otherOption.setVisibility(View.GONE);

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(GCMConstants.DATA_Registered, false)){
            ImageView im = (ImageView)findViewById(R.id.home_registerButton);
            TextView text = (TextView)findViewById(R.id.home_registration_caption);
            im.setBackgroundResource(R.drawable.icon_unregistration);
            text.setText("Unregister");
        }


    }

    private void animateContact(){
        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_fav_layout);
        final LinearLayout l2 = (LinearLayout)findViewById(R.id.home_allcontact_layout);
        ObjectAnimator animY = ObjectAnimator.ofFloat(l1, "translationY", -1000f, 0f);
        animY.setDuration(300);
        animY.setInterpolator(new OvershootInterpolator((float) 1.5));
        animY.setRepeatCount(0);
        animY.start();
        ObjectAnimator animY2 = ObjectAnimator.ofFloat(l2, "translationY", -1000f, 0f);
        animY2.setDuration(500);
        animY2.setInterpolator(new OvershootInterpolator());
        animY2.setRepeatCount(0);
        animY2.start();


    }

    private void animateUpdate(){
        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_moderator_layout);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.home_register_layout);
        LinearLayout l3 = (LinearLayout)findViewById(R.id.home_log_layout);

        ObjectAnimator animY = ObjectAnimator.ofFloat(l1, "translationY", -1000f, 0f);
        animY.setDuration(300);
        animY.setInterpolator(new AccelerateInterpolator((float) 1.5));
        animY.setRepeatCount(0);
        animY.start();
        ObjectAnimator animY2 = ObjectAnimator.ofFloat(l2, "translationY", -1000f, 0f);
        animY2.setDuration(500);
        animY2.setInterpolator(new OvershootInterpolator());
        animY2.setRepeatCount(0);
        animY2.start();
        ObjectAnimator animY3 = ObjectAnimator.ofFloat(l3, "translationY", -1000f, 0f);
        animY3.setDuration(700);
        animY3.setInterpolator(new OvershootInterpolator());
        animY3.setRepeatCount(0);
        animY3.start();


    }

    private void animateOther(){
        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_settings_layout);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.home_otherinfo_layout);

        ObjectAnimator animY = ObjectAnimator.ofFloat(l1, "translationY", -1000f, 0f);
        animY.setDuration(300);
        animY.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY.setRepeatCount(0);
        animY.start();
        ObjectAnimator animY2 = ObjectAnimator.ofFloat(l2, "translationY", -1000f, 0f);
        animY2.setDuration(500);
        animY2.setInterpolator(new OvershootInterpolator());
        animY2.setRepeatCount(0);
        animY2.start();



    }

    public void animatTransport(){
        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_viewmore_layout);
        TextView t1 = (TextView)findViewById(R.id.home_shuttleName_text);
        TextView t2 = (TextView)findViewById(R.id.home_shuttle_note);
        TextView t3 = (TextView)findViewById(R.id.home_shuttle_timeleft);
        TextView t4 = (TextView)findViewById(R.id.home_nextShuttle);

        ObjectAnimator animY = ObjectAnimator.ofFloat(l1, "translationY", -1000f, 0f);
        animY.setDuration(300);
        animY.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY.setRepeatCount(0);
        animY.start();
        ObjectAnimator animY2 = ObjectAnimator.ofFloat(t3, "translationY", -1000f, 0f);
        animY2.setDuration(400);
        animY2.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY2.setRepeatCount(0);
        animY2.start();
        ObjectAnimator animY3 = ObjectAnimator.ofFloat(t4, "translationY", -1000f, 0f);
        animY3.setDuration(500);
        animY3.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY3.setRepeatCount(0);
        animY3.start();
        ObjectAnimator animY4 = ObjectAnimator.ofFloat(t1, "translationY", -1000f, 0f);
        animY4.setDuration(600);
        animY4.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY4.setRepeatCount(0);
        animY4.start();
        ObjectAnimator animY5 = ObjectAnimator.ofFloat(t2, "translationY", -1000f, 0f);
        animY5.setDuration(600);
        animY5.setInterpolator(new OvershootInterpolator ((float) 1.5));
        animY5.setRepeatCount(0);
        animY5.start();

    }

    //Timer functions

    private void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //Content Here
            //TODO
            changeShuttleText();

        }
    };
    public void changeShuttleText(){
        String tempText;
        if (isBuggy==1){tempText="Next Buggy from";}
        else {tempText="Next Shuttle from";}
        shuttleNote.setText(tempText);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new helper_shuttles().NextTransport(transportFROM, transportTO, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        nextShuttle.setText(dformat.format(nextDate.getTime()));

        float[] Difference = new helper_shuttles().TimeLeft(format.format(c2.getTime()), format.format(nextDate.getTime()));

        timeleft.setText("" + ((int) Difference[2]) + " Hrs " + ((int) Difference[1]) + " Min " + ((int) Difference[0])+" Sec left");

        String tempString = transportFROM.toUpperCase()+"-"+transportTO.toUpperCase();
        shuttleName.setText(tempString);

        int hurryUp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SettingsConstants.HOME_HURRYUP_COLOR,5);
        if (Difference[1]<hurryUp && Difference[2]==0.0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nextShuttle.setTextColor(getResources().getColor(R.color.home_hurryup_color,getTheme()));
            }
            else
            {nextShuttle.setTextColor(getResources().getColor(R.color.home_hurryup_color));}
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nextShuttle.setTextColor(getResources().getColor(R.color.home_shuttle_info_color,getTheme()));
            }
            else
            {nextShuttle.setTextColor(getResources().getColor(R.color.home_shuttle_info_color));}

        }
     }

}
