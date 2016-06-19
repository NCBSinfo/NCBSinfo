package com.rohitsuratekar.NCBSinfo.common.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.online.fragments.RegisterFragment;

public class CustomNavigationView {

    public CustomNavigationView(NavigationView navigationView, final Activity activity, final CurrentMode mode) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        navigationView.inflateHeaderView(mode.getDrawerHeader());
        navigationView.inflateMenu(mode.getDrawerMenu());
        MenuItem currentMenu = navigationView.getMenu().findItem(R.id.nav_dashboard);
        if (currentMenu != null) {
            currentMenu.setTitle(pref.getString(RegisterFragment.USERNAME, "User").trim().split(" ")[0] + "\'s " + activity.getString(R.string.dashboard));
        }
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.Navigation_Name);
        TextView email = (TextView) header.findViewById(R.id.Navigation_Email);
        ImageView switchButton = (ImageView) header.findViewById(R.id.switch_mode);
        if (name != null) {
            name.setText(pref.getString(RegisterFragment.USERNAME, "User Name"));
            email.setText(pref.getString(RegisterFragment.EMAIL, "email@domain.com"));
        }
        if (switchButton != null) {
            switchButton.setImageResource(mode.getIcon());
            switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle(activity.getString(R.string.warning_mode_change));
                    alertDialog.setMessage(mode.getSwitchModeMessage());
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pref.edit().remove(Home.MODE).apply();
                            activity.startActivity(new Intent(activity, Home.class));
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Not sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }

    }


}
