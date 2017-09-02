package com.rohitsuratekar.NCBSinfo.activities.dashboard.notifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Splash;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Notifications extends BaseActivity {

    public static final String ACTION = "notificationReceived";

    @BindView(R.id.notification_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.notification_icon)
    ImageView icon;
    @BindView(R.id.notification_message)
    TextView message;
    boolean directlyOpened;


    private List<NotificationModel> modelList = new ArrayList<>();
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //Check if it is directly opened
        directlyOpened = getIntent().getAction() != null;

        modelList = new NotificationData(getBaseContext()).getAll();
        adapter = new NotificationAdapter(modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnNotificationClick(new NotificationAdapter.OnNotificationClick() {
            @Override
            public void showFull(View v, int position) {
                showNotificationDialog(position);
            }
        });

        if (modelList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.NOTIFICATIONS;
    }

    void showNotificationDialog(final int position) {
        final NotificationModel model = modelList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(model.getTitle())
                .setMessage(model.getMessage())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new NotificationData(getBaseContext()).delete(model);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();

    }

    @Override
    public void onBackPressed() {
        if (directlyOpened) {
            Intent intent = new Intent(Notifications.this, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            animateTransition();
        } else {
            super.onBackPressed();
        }
    }
}
