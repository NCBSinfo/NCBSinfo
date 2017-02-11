package com.rohitsuratekar.NCBSinfo.activities.settings.log;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogActivity extends AppCompatActivity {

    @BindView(R.id.log_recycler)
    RecyclerView recyclerView;

    private List<String> allLogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        ButterKnife.bind(this);
        setTitle("Log");

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(":")) {
                    allLogs.add(line);
                }
            }
        } catch (IOException e) {
            Log.error("Something is wrong");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        LogAdapter adapter = new LogAdapter(allLogs);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
