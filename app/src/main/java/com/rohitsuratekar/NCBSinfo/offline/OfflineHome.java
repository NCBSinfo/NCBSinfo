package com.rohitsuratekar.NCBSinfo.offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.lecturehalls.LectureHalls;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;

public class OfflineHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_home);

        Intent intent = new Intent(this, OnlineHome.class);
        intent.putExtra(Transport.INDENT,"0");
        startActivity(intent);
    }
}
