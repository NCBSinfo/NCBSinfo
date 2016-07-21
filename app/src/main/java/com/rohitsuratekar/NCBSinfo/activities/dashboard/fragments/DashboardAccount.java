package com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoardAdapter;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoardModel;
import com.rohitsuratekar.NCBSinfo.activities.login.ChangePassword;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.NotificationPreference;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.TransportPreference;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
import com.rohitsuratekar.NCBSinfo.background.firebase.FireBaseConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.ui.ScrollUpRecyclerView;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 13-07-16.
 */
public class DashboardAccount extends Fragment {


    private final String TAG = getClass().getSimpleName();
    ScrollUpRecyclerView recyclerView;
    List<DashBoardModel> fullList;
    DashBoardAdapter adapter;
    String[] routesArray;
    Preferences pref;
    BaseParameters baseParameters;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.base_animated_fragment, container, false);

        recyclerView = (ScrollUpRecyclerView) rootView.findViewById(R.id.base_animated_recyclerView);
        routesArray = getResources().getStringArray(R.array.home_spinner_items);
        pref = new Preferences(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        baseParameters = new BaseParameters(getContext());

        fullList = new ArrayList<>();


        adapter = new DashBoardAdapter(fullList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new DashBoardAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                String currentField = fullList.get(position).getFieldName();
                if (currentField.equals(getString(R.string.dashboard_name))) {
                    changeName(position);
                } else if (currentField.equals(getString(R.string.dashboard_default_route))) {
                    Intent intent = new Intent(getContext(), Settings.class);
                    intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, TransportPreference.class.getName());
                    intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                } else if (currentField.equals(getString(R.string.dashboard_events_history))) {
                    showNumberPicker();
                } else if (currentField.equals(getString(R.string.dashboard_notifications))) {
                    Intent intent = new Intent(getContext(), Settings.class);
                    intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, NotificationPreference.class.getName());
                    intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                } else if (currentField.equals(getString(R.string.dashboard_password))) {
                    changePass();
                }

            }
        });

        updateAdapter();

        return rootView;
    }

    @SuppressWarnings("")
    private void changeName(int position) {
        if (new General().isNetworkAvailable(getContext())) {
            if (!new General().isOnProxy()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                alert.setMessage("You are about to change your current " + fullList.get(position).getFieldName() + " \'" +
                        fullList.get(position).getFieldValue() + "\'. Enter new " + fullList.get(position).getFieldName() + " below and click \'Change\'");
                alert.setTitle("Change " + fullList.get(position).getFieldName());
                alert.setView(edittext);
                alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (mAuth.getCurrentUser() != null) {
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Changing name...");
                            progressDialog.show();
                            mDatabase.child(FireBaseConstants.USER_NODE)
                                    .child(new DataManagement().makePath(mAuth.getCurrentUser().getEmail()))
                                    .child(FireBaseConstants.NAME).setValue(edittext.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        pref.user().setName(edittext.getText().toString());
                                        Toast.makeText(getContext(), "Name changed successfully", Toast.LENGTH_LONG).show();
                                        updateAdapter();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        Log.e(TAG, task.getException().getLocalizedMessage());
                                    }

                                }
                            })
                            ;

                        } else {
                            Toast.makeText(getContext(), "Authentication Problem, Try after some time", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Proxy network warning!")
                        .setMessage("It looks like you are using proxy network : \"" + new General().getProxyAddress() +
                                "\" . Unfortunately, our database currently don't support proxy :( \nPlease use non proxy network to change name. "
                        )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        } else {
            Toast.makeText(getContext(), "Internet connection is needed for this action", Toast.LENGTH_LONG).show();
        }
    }

    private void changePass() {
        if (new General().isNetworkAvailable(getContext())) {
            startActivity(new Intent(getContext(), ChangePassword.class));
            getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
        } else {
            Toast.makeText(getContext(), "Internet connection is needed for this action", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();

    }

    private void updateAdapter() {
        fullList.clear();
        fullList.add(new DashBoardModel(getString(R.string.dashboard_name), pref.user().getName(), R.drawable.icon_dashboard_pin, true));
        fullList.add(new DashBoardModel(getString(R.string.dashboard_email), pref.user().getEmail(), R.drawable.icon_email, false));
        fullList.add(new DashBoardModel(getString(R.string.dashboard_password), "******", R.drawable.icon_password, true));
        fullList.add(new DashBoardModel(getString(R.string.dashboard_default_route), routesArray[pref.user().getDefaultRouteValue()], R.drawable.icon_shuttle, true));
        fullList.add(new DashBoardModel(getString(R.string.dashboard_notifications), pref.settings().getNotificationPreferenceStatus(), R.drawable.icon_notification, true));
        fullList.add(new DashBoardModel(getString(R.string.dashboard_events_history), pref.user().getNumberOfEventsToKeep() + " Events", R.drawable.icon_updates, true));
        adapter.notifyDataSetChanged();
    }


    private void showNumberPicker() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        final NumberPicker picker = new NumberPicker(getContext());
        picker.setMaxValue(100);
        picker.setMinValue(10);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicker = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicker.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(picker, numPicker);
        picker.setValue(pref.user().getNumberOfEventsToKeep());
        new AlertDialog.Builder(getContext())
                .setTitle("Events history")
                .setMessage("Maximum number of events stored on this device")
                .setView(relativeLayout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pref.user().setNumberOfEventsToKeep(picker.getValue());
                        updateAdapter();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        })
                .show();
    }
}

