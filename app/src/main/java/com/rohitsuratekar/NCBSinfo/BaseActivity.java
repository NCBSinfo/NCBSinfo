package com.rohitsuratekar.NCBSinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.common.OnRouteSelect;
import com.rohitsuratekar.NCBSinfo.fragments.contacts.ContactModel;
import com.rohitsuratekar.NCBSinfo.fragments.contacts.ContactSheet;
import com.rohitsuratekar.NCBSinfo.fragments.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.fragments.home.Home;
import com.rohitsuratekar.NCBSinfo.fragments.informtion.Information;
import com.rohitsuratekar.NCBSinfo.fragments.settings.Settings;
import com.rohitsuratekar.NCBSinfo.fragments.settings.SettingsActions;
import com.rohitsuratekar.NCBSinfo.fragments.settings.SettingsInfo;
import com.rohitsuratekar.NCBSinfo.fragments.transport.Transport;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportDetails;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportSheet;

import java.util.List;

import static android.R.string.cancel;
import static android.R.string.ok;

public class BaseActivity extends AppCompatActivity implements OnRouteSelect, Transport.OnHomeInteraction,
        Contacts.OnContactSelected, ContactSheet.OnContactSheetSelected, Settings.OnSettingAction {

    private static final int CALL_PERMISSION = 1989;

    BottomNavigationView navigationView;
    FrameLayout progressBar;
    FrameLayout mainFrame;
    BaseViewModel viewModel;
    TextView mainWarning;
    List<TransportDetails> transportList;
    TransportDetails currentTransport;
    private String tempCallString = "";

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
        subscribe();
    }

    private void subscribe() {
        viewModel.getAllTransport().observe(this, new Observer<List<TransportDetails>>() {
            @Override
            public void onChanged(@Nullable List<TransportDetails> transportDetailsList) {
                if (transportDetailsList != null) {
                    transportList = transportDetailsList;
                    currentTransport = transportList.get(0);
                    mainFrame.setVisibility(View.VISIBLE);
                    navigationView.setVisibility(View.VISIBLE);
                    mainWarning.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    attachHome();
                }
            }
        });
    }

    public TransportDetails getCurrentTransport() {
        return currentTransport;
    }

    public List<TransportDetails> getTransportList() {
        return transportList;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    attachHome();
                    break;
                case R.id.nav_transport:
                    replaceFragment(new Transport());
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

    public void attachTransport() {
        navigationView.setSelectedItemId(R.id.nav_transport);
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

    public void showRouteList() {
        BottomSheetDialogFragment bottomSheetDialogFragment;
        if (transportList != null) {
            bottomSheetDialogFragment = TransportSheet.newInstance(currentTransport.getRouteID(), currentTransport.getReturnIndex());
        } else {
            bottomSheetDialogFragment = TransportSheet.newInstance(-1, -1);
        }
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void routeSelected(int routeID) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        for (TransportDetails t : transportList) {
            if (t.getRouteID() == routeID) {
                currentTransport = t;
                break;
            }
        }
        if (f instanceof Home) {
            ((Home) f).changeRoute(routeID);
        } else if (f instanceof Transport) {
            ((Transport) f).changeRoute(routeID);
        }
    }

    @Override
    public void routeSwap(TransportDetails newRoute) {
        currentTransport = newRoute;
    }

    @Override
    public void contactSelected(ContactModel contact) {
        BottomSheetDialogFragment bottomSheetDialogFragment = ContactSheet.newInstance(contact);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    @Override
    public void onCalled(String call) {
        //Check permissions
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            call(call);
        } else {
            tempCallString = call;
            showDenied();
        }
    }

    @SuppressLint("MissingPermission")
    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private static final String[] permissions = {
            Manifest.permission.CALL_PHONE
    };

    private void showDenied() {
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle(getString(R.string.ct_permission_needed))
                .setMessage(getString(R.string.ct_warning_permission))
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BaseActivity.this, permissions, CALL_PERMISSION);
                    }
                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(tempCallString);
                } else {
                    Log.e("Contacts", "Permission Denied");
                    showDenied();
                }
                break;
        }
    }

    @Override
    public void settingAction(int actionID, String params) {
        switch (actionID) {
            case SettingsActions.ACTION_RESET_ROUTE:
                Toast.makeText(getBaseContext(), "All routes reset!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BaseActivity.this, BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
                break;
            case SettingsActions.ACTION_INFO:
                Intent infoIntent = new Intent(this, SettingsInfo.class);
                infoIntent.setAction(params);
                startActivity(infoIntent);
                break;
            case SettingsActions.ACTION_REMOVE_EGG:
                navigationView.setSelectedItemId(R.id.nav_home);
                break;
        }
    }
}
