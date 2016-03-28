package com.rohitsuratekar.NCBSinfo;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rohitsuratekar.NCBSinfo.activity.Activity_Contact;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMModeration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMRegistration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_LectureHalls;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Shuttles;
import com.rohitsuratekar.NCBSinfo.constants.DatabaseConstants;

import java.io.File;

public class Home extends AppCompatActivity implements View.OnClickListener{

    Animation alphaAnimation, exitAnimation, dropDown;
    int screen_H;
    LinearLayout shuttleSection, shuttleAnimation, shuttleOption, contactSection, contactAnimation, contactOption;
    LinearLayout updatesSection, updatesAnimation, updatesOption, otherSection, otherAnimation, otherOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //Delete Old databases
        if(doesDatabaseExist(this,"ContactStore2")){
            this.deleteDatabase("ContactStore2");
        }


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


            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.home_actionbar);


        }

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


        ImageButton i1 = (ImageButton)findViewById(R.id.home_shuttleButton);
        ImageButton i2 = (ImageButton)findViewById(R.id.home_contactButton);
        ImageButton i3 = (ImageButton)findViewById(R.id.home_updatesButton);
        ImageButton i4 = (ImageButton)findViewById(R.id.home_otherButton);
        i1.setOnClickListener(this);
        i2.setOnClickListener(this);
        i3.setOnClickListener(this);
        i4.setOnClickListener(this);

        SetDefaultView();

        LinearLayout l1 = (LinearLayout)findViewById(R.id.home_fav_layout);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.home_allcontact_layout);

        l1.setOnClickListener(this);
        l2.setOnClickListener(this);








    } //OnCreate close


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.home_shuttleButton:
                GeneralAnimationFlow(v, shuttleSection, shuttleAnimation, shuttleOption);
                break;
            case R.id.home_contactButton:
                GeneralAnimationFlow(v, contactSection, contactAnimation, contactOption);
                break;
            case R.id.home_updatesButton:
                GeneralAnimationFlow(v, updatesSection,updatesAnimation,updatesOption);
                break;
            case R.id.home_otherButton:
                GeneralAnimationFlow(v, otherSection,otherAnimation,otherOption);
                break;
            case R.id.home_fav_layout:
                Intent intent = new Intent(Home.this,Activity_GCMRegistration.class);
                startActivity(intent);
                break;
            case R.id.home_allcontact_layout:
                Intent intent2 = new Intent(Home.this,Activity_GCMModeration.class);
                startActivity(intent2);
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
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // targetView.setVisibility(View.VISIBLE);
                        // targetView.startAnimation(alphaAnimation);
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

    public void GeneralAnimationFlow(View button, View section, View animation, View options ){
        int x = button.getRight();
        int y = button.getBottom();
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


    }


}
