/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;


public class SetUpActivity {

    private boolean tabsEnabled;

    public SetUpActivity(final Activity activity, CurrentActivity currentActivity) {

        AppPrefs prefs = new AppPrefs(activity);

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
                DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                if (activity.getClass() != Login.class) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.startActivity(new Intent(activity, Login.class));
                            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 300);
                }

            }
        });
    }

    public void setTabsEnabled(boolean tabsEnabled) {
        this.tabsEnabled = tabsEnabled;
    }
}
