package com.rohitsuratekar.NCBSinfo.activities.experimental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.canteen.Canteen;
import com.rohitsuratekar.NCBSinfo.activities.locations.LectureHalls;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Experimental extends BaseActivity {
    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.EXPERIMENTAL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GridView grid = (GridView) findViewById(R.id.experimental_grid);
        final String[] names = getResources().getStringArray(R.array.extra_info_icons);
        final int[] icons = {
                R.drawable.icon_lecturehall,
                R.drawable.icon_canteen,
        };
        //Auto adjust columns. Max 3
        if(icons.length<3){
            grid.setNumColumns(icons.length);
        }
        ExperimentalGrid adapter = new ExperimentalGrid(Experimental.this, names, icons);
        grid.setAdapter(adapter);
        grid.setFocusable(false);
        grid.setFocusableInTouchMode(false);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        startActivity(new Intent(Experimental.this, LectureHalls.class));
                        break;
                    case 1:
                        startActivity(new Intent(Experimental.this, Canteen.class));
                        break;
                }
            }
        });
    }
}
