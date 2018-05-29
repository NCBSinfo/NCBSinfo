package com.rohitsuratekar.NCBSinfo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.fragments.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.fragments.home.Home;
import com.rohitsuratekar.NCBSinfo.fragments.informtion.Information;
import com.rohitsuratekar.NCBSinfo.fragments.settings.Settings;
import com.rohitsuratekar.NCBSinfo.fragments.transport.Transport;

public class BaseActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FrameLayout progressBar;
    FrameLayout mainFrame;
    BaseViewModel viewModel;
    TextView mainWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mainWarning = findViewById(R.id.main_warning);
        mainFrame = findViewById(R.id.main_frame);
        navigationView = findViewById(R.id.nav_bar);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        progressBar = findViewById(R.id.progress);
        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        mainFrame.setVisibility(View.GONE);
        navigationView.setVisibility(View.GONE);
        viewModel.loadApp(getApplicationContext());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    attachHome();
                    break;
                case R.id.nav_transport:
                    attachTransport();
                    break;
                case R.id.nav_contacts:
                    replaceFragment(new Contacts());
                    break;
                case R.id.nav_info:
                    replaceFragment(new Information());
                    break;
                case R.id.nav_settings:
                    replaceFragment(new Settings());
                    break;
            }
            return true;
        }
    };

    private void attachHome() {
        replaceFragment(new Home());
    }

    private void attachTransport() {
        replaceFragment(new Transport());
    }


    /**
     * Replaces fragment only when that fragment is not present in backstack
     * Source: https://stackoverflow.com/questions/18305945/how-to-resume-fragment-from-backstack-if-exists
     *
     * @param fragment: Fragment to replace
     */
    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_frame, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_frame);
            if (f instanceof Home) {
                navigationView.setSelectedItemId(R.id.nav_home);
            } else if (f instanceof Transport) {
                navigationView.setSelectedItemId(R.id.nav_transport);
            } else if (f instanceof Information) {
                navigationView.setSelectedItemId(R.id.nav_info);
            } else if (f instanceof Contacts) {
                navigationView.setSelectedItemId(R.id.nav_contacts);
            } else if (f instanceof Settings) {
                navigationView.setSelectedItemId(R.id.nav_settings);
            }
        }
    }
}
