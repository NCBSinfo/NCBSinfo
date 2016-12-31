/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;


public class SetUpActivity {

    private static final String TAG = "SetUpActivity";
    private ColorScheme colorScheme;
    private Activity activity;

    public SetUpActivity(Activity activity, CurrentActivity currentActivity) {
        colorScheme = new ColorScheme(activity);
        this.activity = activity;
        ViewStub viewStub = (ViewStub) activity.findViewById(R.id.base_view);
        viewStub.setLayoutResource(currentActivity.getLayout());
        viewStub.inflate();
        activity.setTitle(activity.getString(currentActivity.getName()));
        TabLayout tabs = (TabLayout) activity.findViewById(R.id.tabs);
        if (!currentActivity.isTabEnabled()) {
            tabs.setVisibility(View.GONE);
        }

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.inflateHeaderView(R.layout.base_header); //Header
        navigationView.inflateMenu(R.menu.base_drawer);
        View header = navigationView.getHeaderView(0);
        GradientDrawable backgroundGradient = (GradientDrawable) header.getBackground();
        backgroundGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        backgroundGradient.setGradientCenter(10, 0);
        TextView name = (TextView) header.findViewById(R.id.nav_header_name);
        TextView email = (TextView) header.findViewById(R.id.nav_header_email);
        ImageView icon = (ImageView) header.findViewById(R.id.nav_header_icon);


        //For Coloring
        Toolbar bar = (Toolbar) activity.findViewById(R.id.toolbar);
        //Colors
        backgroundGradient.setColor(General.getColor(activity, colorScheme.getPrimary()));
        bar.setBackgroundColor(General.getColor(activity, colorScheme.getPrimary()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(General.getColor(activity, colorScheme.getPrimaryDark()));
        }
    }

}
