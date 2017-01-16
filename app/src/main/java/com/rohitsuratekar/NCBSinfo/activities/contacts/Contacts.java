package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contacts extends BaseActivity {

    @BindView(R.id.contact_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.contact_all_contacts)
    Button showAllButton;
    @BindView(R.id.contact_current_name)
    TextView currentName;
    @BindView(R.id.contact_current_number)
    TextView currentNumber;
    @BindView(R.id.contact_current_icon)
    ImageView currentIcon;
    @BindView(R.id.contacts_main_layout)
    ConstraintLayout mainLayout;

    List<ContactModel> modelList;
    ContactHomeAdapter adapter;
    ContactModel currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        modelList = new ArrayList<>();
        currentContact = new ContactModel("emergency", "129891283", R.drawable.icon_star);
        modelList.add(new ContactModel("Reception", "129891283", R.drawable.icon_star));
        modelList.add(new ContactModel("Medical Centre", "129891283", R.drawable.icon_medical));
        modelList.add(new ContactModel("SubStation", "129891283", R.drawable.icon_light));
        modelList.add(new ContactModel("SubStation", "129891283", R.drawable.icon_light));

        adapter = new ContactHomeAdapter(modelList);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeItem(0, true);
            }
        });

        adapter.setOnItemClickListener(new ContactHomeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                swipeItem(position, true);
            }
        });

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            protected void onSwipeRight() {
                swipeItem(0, true);
            }

            @Override
            protected void onSwipeLeft() {
                swipeItem(modelList.size() - 1, false);
            }

            @Override
            protected void onSwipeTop() {
                swipeItem(0, true);
            }

            @Override
            protected void onSwipeBottom() {
                swipeItem(modelList.size() - 1, false);
            }
        });

    }

    private void swipeItem(int item, boolean isUp) {
        if (isUp) {
            modelList.add(currentContact);
            currentContact = modelList.get(item);
            modelList.remove(item);
        } else {
            modelList.add(0, currentContact);
            currentContact = modelList.get(item + 1);
            modelList.remove(item + 1);
        }
        adapter.notifyDataSetChanged();
        setUpCurrent();
    }

    private void setUpCurrent() {
        currentName.setText(currentContact.getName().toLowerCase());
        currentNumber.setText(currentContact.getNumber());
        currentIcon.setImageResource(currentContact.getIcon());
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.CONTACTS;
    }
}
