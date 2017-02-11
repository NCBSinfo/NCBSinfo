/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.background.tasks.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoadRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.OnTaskCompleted;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;


class SetUpActivity {

    private boolean tabsEnabled;
    private Activity activity;
    private AppPrefs prefs;

    SetUpActivity(final Activity activity, CurrentActivity currentActivity) {

        this.activity = activity;
        prefs = new AppPrefs(activity);

        ViewStub viewStub = (ViewStub) activity.findViewById(R.id.base_view);
        viewStub.setLayoutResource(currentActivity.getLayout());
        viewStub.inflate();
        activity.setTitle(currentActivity.getName());
        TabLayout tabs = (TabLayout) activity.findViewById(R.id.tabs);
        if (!tabsEnabled) {
            tabs.setVisibility(View.GONE);
        }

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.inflateHeaderView(R.layout.base_header); //Header
        navigationView.inflateMenu(R.menu.base_drawer);
        View header = navigationView.getHeaderView(0);

        GradientDrawable backgroundGradient = (GradientDrawable) header.getBackground();
        backgroundGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        backgroundGradient.setGradientCenter(10, 0);
        backgroundGradient.setColor(General.getColor(activity, R.color.colorPrimary));

        TextView name = (TextView) header.findViewById(R.id.nav_header_name);
        TextView email = (TextView) header.findViewById(R.id.nav_header_email);
        TextView optionText = (TextView) header.findViewById(R.id.nav_header_option_text);
        ImageView optionIcon = (ImageView) header.findViewById(R.id.nav_header_option);

        TextView footerText = (TextView) navigationView.findViewById(R.id.nav_footer_text);
        ImageView footerIcon = (ImageView) navigationView.findViewById(R.id.nav_footer_icon);
        ConstraintLayout layout = (ConstraintLayout) navigationView.findViewById(R.id.footer_layout);
        layout.setBackground(navigationView.getBackground()); // To keep same color


        if (prefs.isUserLoggedIn()) {
            name.setText(prefs.getUsername());
            email.setText(prefs.getUserEmail());
            optionText.setText(activity.getString(R.string.log_out));
            optionIcon.setImageResource(R.drawable.icon_authenticated);
            optionIcon.setColorFilter(General.getColor(activity, R.color.green));
            footerText.setText(prefs.getLastSync());
            footerIcon.setImageResource(R.drawable.icon_sync);
        } else {
            name.setText(activity.getString(R.string.app_name));
            email.setText(prefs.getCurrentVersionName());
            optionIcon.setImageResource(R.drawable.icon_unauthenticated);
            optionText.setText(activity.getString(R.string.log_in));
            optionIcon.setColorFilter(General.getColor(activity, R.color.colorPrimaryLight));
            footerText.setText(activity.getString(R.string.last_sync_warning));
            footerIcon.setImageResource(R.drawable.icon_no_sync);
        }

        optionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 20-01-2017
                optionClick();
            }
        });
    }

    public void setTabsEnabled(boolean tabsEnabled) {
        this.tabsEnabled = tabsEnabled;
    }

    private void optionClick() {

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (activity.getClass() != Login.class) {
            if (prefs.isUserLoggedIn()) {
                new AlertDialog.Builder(activity)
                        .setTitle("Are you sure?")
                        .setMessage(activity.getString(R.string.log_out_warning))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ProgressDialog progressDialog = new ProgressDialog(activity);
                                progressDialog.setMessage("Signing you out...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                signOut(progressDialog);
                            }
                        })
                        .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.startActivity(new Intent(activity, Login.class));
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 300);
            }
        }
    }

    private void signOut(final ProgressDialog dialog) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        prefs.clear();
        prefs.appOpened();
        prefs.updateVersion();
        new CreateDefaultRoutes(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {
                new LoadRoutes(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted() {
                        dialog.dismiss();
                        gotoHome();
                    }
                }).execute(activity);
            }
        }).execute(activity);
    }


    private void gotoHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 300);
    }

}
