package com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoardAdapter;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.DashBoardModel;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.TransportPreference;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
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

    ScrollUpRecyclerView recyclerView;
    List<DashBoardModel> fullList;
    DashBoardAdapter adapter;
    String defaultRoute;
    String[] routesArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.base_animated_fragment, container, false);

        recyclerView = (ScrollUpRecyclerView) rootView.findViewById(R.id.base_animated_recyclerView);
        routesArray = getResources().getStringArray(R.array.home_spinner_items);
        defaultRoute = routesArray[new Preferences(getContext()).user().getDefaultRoute()];

        fullList = new ArrayList<>();
        setList();

        adapter = new DashBoardAdapter(fullList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DashBoardAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (fullList.get(position).getFieldName().equals("Name")) {
                    changeName(position);
                } else {
                    Intent intent = new Intent(getContext(), Settings.class);
                    intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, TransportPreference.class.getName());
                    intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                    startActivity(intent);
                }

            }
        });

        return rootView;
    }

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
                        //OR
                        String YouEditTextValue = edittext.getText().toString();
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

    @Override
    public void onResume() {
        super.onResume();
        defaultRoute = routesArray[new Preferences(getContext()).user().getDefaultRoute()];
        setList();
        adapter.notifyDataSetChanged();

    }

    private void setList() {
        fullList.clear();
        fullList.add(new DashBoardModel("Name", "Rohit Suratekar", R.drawable.icon_profile, true));
        fullList.add(new DashBoardModel("Email", "rohitsuratekar@gmail.com", R.drawable.icon_email, false));
        fullList.add(new DashBoardModel("Default Route", defaultRoute, R.drawable.icon_shuttle, true));
        fullList.add(new DashBoardModel("Notifications", "General", R.drawable.icon_wifi_on, true));
    }
}

