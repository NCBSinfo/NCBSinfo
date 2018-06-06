package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Locations extends AppCompatActivity {

    @BindView(R.id.loc_recycler)
    RecyclerView recyclerView;


    @BindView(R.id.loc_img_name)
    ImageView byName;
    @BindView(R.id.loc_img_building)
    ImageView byBuilding;
    @BindView(R.id.loc_img_floor)
    ImageView byFloor;
    @BindView(R.id.loc_img_whatever)
    ImageView byWhatever;

    @BindView(R.id.loc_txt_name)
    TextView nameText;
    @BindView(R.id.loc_txt_build)
    TextView buildText;
    @BindView(R.id.loc_txt_floor)
    TextView floorText;
    @BindView(R.id.loc_txt_whatever)
    TextView whatText;


    private LocationAdapter adapter;
    private List<LocationModel> locationModels = new ArrayList<>();
    private boolean nameAscend = true;
    private boolean buildAscend = true;
    private boolean floorAscend = true;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);
        setTitle(R.string.locations);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getBaseContext());
        locationModels = new LocationList().getLocations();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        adapter = new LocationAdapter(locationModels);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        switch (prefs.getLocationSort()) {
            case 0:
                sortByName();
                break;
            case 1:
                sortByBuildings();
                break;
            case 2:
                sortByFloor();
                break;
            case 3:
                sortByWhatever();
                break;
            default:
                sortByName();
        }

    }

    @OnClick({R.id.loc_img_name, R.id.loc_txt_name})
    public void sortByName() {
        Comparator<LocationModel> comparator = new Comparator<LocationModel>() {
            @Override
            public int compare(LocationModel left, LocationModel right) {
                if (nameAscend) {
                    return left.getName().compareTo(right.getName());
                } else {
                    return right.getName().compareTo(left.getName());
                }
            }
        };
        Collections.sort(locationModels, comparator);
        adapter.notifyDataSetChanged();
        select(byName, nameText);
        nameAscend = !nameAscend;
        buildAscend = true;
        floorAscend = true;
        prefs.setLocationSort(0);
    }

    @OnClick({R.id.loc_img_building, R.id.loc_txt_build})
    public void sortByBuildings() {
        Comparator<LocationModel> comparator = new Comparator<LocationModel>() {
            @Override
            public int compare(LocationModel left, LocationModel right) {
                if (buildAscend) {
                    return left.getBuilding().compareTo(right.getBuilding()); // use your logic
                } else {
                    return right.getBuilding().compareTo(left.getBuilding()); // use your logic
                }
            }
        };
        Collections.sort(locationModels, comparator);
        adapter.notifyDataSetChanged();
        select(byBuilding, buildText);
        nameAscend = true;
        buildAscend = !buildAscend;
        floorAscend = true;
        prefs.setLocationSort(1);
    }

    @OnClick({R.id.loc_img_floor, R.id.loc_txt_floor})
    public void sortByFloor() {
        Comparator<LocationModel> comparator = new Comparator<LocationModel>() {
            @Override
            public int compare(LocationModel left, LocationModel right) {
                if (floorAscend) {
                    return left.getFloor() - right.getFloor(); // use your logic
                } else {
                    return right.getFloor() - left.getFloor(); // use your logic
                }
            }
        };
        Collections.sort(locationModels, comparator);
        adapter.notifyDataSetChanged();
        select(byFloor, floorText);
        nameAscend = true;
        buildAscend = true;
        floorAscend = !floorAscend;
        prefs.setLocationSort(2);
    }

    @OnClick({R.id.loc_img_whatever, R.id.loc_txt_whatever})
    public void sortByWhatever() {
        long seed = System.nanoTime();
        Collections.shuffle(locationModels, new Random(seed));
        adapter.notifyDataSetChanged();
        select(byWhatever, whatText);
        prefs.setLocationSort(3);
    }

    private void resetAll() {
        resetImage(byName, byBuilding, byFloor, byWhatever);
        resetText(nameText, buildText, floorText, whatText);
    }

    private void resetImage(ImageView... list) {
        for (ImageView imageView : list) {
            imageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLight));
            imageView.setAlpha((float) 0.8);
        }
    }

    private void resetText(TextView... list) {
        for (TextView textView : list) {
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLight));
            textView.setAlpha((float) 0.8);
        }
    }

    private void select(ImageView imageView, TextView textView) {
        resetAll();
        imageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        imageView.setAlpha((float) 1);
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        textView.setAlpha((float) 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
