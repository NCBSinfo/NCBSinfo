package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTransport extends BaseActivity implements LifecycleRegistryOwner {

    @BindView(R.id.et_view_holder)
    FrameLayout layout;

    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String TYPE = "type";
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private ETViewModel viewModel;
    private int currentFragmentIndex = 0;
    private List<RouteData> routeData = new ArrayList<>();
    private boolean isForEdit = false;
    private String[] editDetails = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transport);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        viewModel = ViewModelProviders.of(this).get(ETViewModel.class);
        String ori = getIntent().getStringExtra(ORIGIN);
        String des = getIntent().getStringExtra(DESTINATION);
        String typ = getIntent().getStringExtra(TYPE);
        if (ori != null && des != null && typ != null) {
            setTitle(R.string.edit_transport);
            isForEdit = true;
            editDetails[0] = ori;
            editDetails[1] = des;
            editDetails[2] = typ;
            viewModel.editActivityTask(getApplicationContext(), editDetails);
        } else {
            setTitle(R.string.add_transport);
        }

        attachViewModels();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    public List<RouteData> getRouteData() {
        return routeData;
    }

    private void attachViewModels() {
        viewModel.getCurrentStep().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    integer = 0;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //Animation when fragment changes
                if (currentFragmentIndex > integer) {
                    transaction.setCustomAnimations(R.anim.fragment_pop_enter, R.anim.fragment_pop_exit, R.anim.fragment_enter, R.anim.fragment_exit);
                } else {
                    transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
                }
                Fragment fragment;
                switch (integer) {
                    case 1:
                        fragment = new ETTypeFragment();
                        break;
                    case 2:
                        fragment = new ETFrequencyFragment();
                        break;
                    case 3:
                        fragment = new ETTripsFragment();
                        break;
                    case 4:
                        fragment = new ETFirstTripFragment();
                        break;
                    case 5:
                        fragment = new ETConfirmFragment();
                        break;
                    default:
                        fragment = new ETBasicFragment();
                }
                transaction.replace(R.id.et_view_holder, fragment);
                transaction.commit();
                currentFragmentIndex = integer;
            }
        });


        viewModel.getRouteList().observe(this, new Observer<List<RouteData>>() {
            @Override
            public void onChanged(@Nullable List<RouteData> routeDataList) {
                if (routeDataList != null) {
                    routeData.clear();
                    routeData.addAll(routeDataList);
                }
            }
        });

        viewModel.getEditSetup().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        updateETData();
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragmentIndex == 0) {
                super.onBackPressed();
                animateTransition();
            } else {

                new Builder(EditTransport.this)
                        .setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.et_back_press))
                        .setPositiveButton(getString(R.string.et_back_cancel), new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                animateTransition();
                            }
                        }).setNegativeButton(getString(R.string.et_back_previous), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.getCurrentStep().postValue(currentFragmentIndex - 1);
                    }
                }).show();
            }
        }
    }

    private void updateETData() {
        final ETDataHolder data = viewModel.getData().getValue();
        if (data != null) {
            if (data.getTripData().size() > 1) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditTransport.this);
                LayoutInflater inflater = getLayoutInflater();
                final View convertView = (View) inflater.inflate(R.layout.manage_transport_dialog, null);
                TextView message = ButterKnife.findById(convertView, R.id.mt_dialog_message);
                message.setText(getString(R.string.mt_select_trip_message,
                        data.getOrigin().toUpperCase(), data.getDestination().toUpperCase()));
                alertDialog.setView(convertView);
                alertDialog.setCancelable(false);
                final RadioGroup group = ButterKnife.findById(convertView, R.id.mt_radio_group);
                group.setOrientation(RadioGroup.VERTICAL);
                final Calendar calendar = Calendar.getInstance();
                for (TripData t : data.getTripData()) {
                    RadioButton button = new RadioButton(getApplicationContext());
                    button.setId(data.getTripData().indexOf(t));
                    calendar.set(Calendar.DAY_OF_WEEK, t.getDay());
                    if (data.isRegular() && t.getDay() != Calendar.SUNDAY) {
                        button.setText(getString(R.string.et_confirm_fq_mon_sat));
                    } else {
                        button.setText(DateConverter.convertToString(calendar, "EEEE"));
                    }
                    group.addView(button);
                }
                final int[] selectedIndex = {-1};
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        selectedIndex[0] = i;
                    }
                });
                alertDialog.setPositiveButton("ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedIndex[0] == -1) {
                            selectedIndex[0] = 0;
                        }
                        General.makeShortToast(getApplicationContext(), getString(R.string.mt_select_snackbar, String.valueOf(selectedIndex[0] + 1)));
                        prepareTrips(selectedIndex[0]);
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();

            } else if (viewModel.getData().getValue().getTripData().size() == 1) {
                prepareTrips(0);
            }
        }
    }

    private void prepareTrips(int index) {
        ETDataHolder data = viewModel.getData().getValue();
        if (data != null) {
            if (data.isRegular()) {
                if (data.getTripData().get(index).getDay() != Calendar.SUNDAY) {
                    data.setFrequency(R.id.et_fq_mon_sat);
                    data.setFrequencyDetails(new int[]{0, 1, 1, 1, 1, 1, 1});
                } else {
                    data.setFrequency(R.id.et_fq_select_specific);
                    data.setFrequencyDetails(new int[]{1, 0, 0, 0, 0, 0, 0});
                }
            } else if (data.getTripData().size() == 1) {
                data.setFrequency(R.id.et_fq_all_days);
                data.setFrequencyDetails(new int[]{1, 1, 1, 1, 1, 1, 1});
            } else {
                data.setFrequency(R.id.et_fq_select_specific);
                int[] temp = new int[7];
                for (int i = 0; i < 7; i++) {
                    if (i + 1 == data.getTripData().get(index).getDay()) { //Sunday starts from 1
                        temp[i] = 1;
                    } else {
                        temp[i] = 0;
                    }
                }
                data.setFrequencyDetails(temp);
            }
            data.setItemList(data.getTripData().get(index).getTrips());
            viewModel.getData().postValue(data);
        }
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_manage_transport;
    }


}
