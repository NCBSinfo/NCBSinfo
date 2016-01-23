package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Intent intent = getIntent();
        String currentSwitch = intent.getExtras().getString("aboutswitch");
        int currentInt = Integer.parseInt(currentSwitch);
        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.AboutMe);
        RelativeLayout r2 = (RelativeLayout)findViewById(R.id.TermsAndConditions);
        RelativeLayout r3 = (RelativeLayout)findViewById(R.id.Feature);
        if (currentInt==0){

            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);

        }
        else if (currentInt==1){
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.GONE);
            TextView trt = (TextView)findViewById(R.id.termsText);
            trt.setText(Html.fromHtml(getString(R.string.terms)));
        }
        else if (currentInt==2){
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.VISIBLE);
            TextView trt = (TextView)findViewById(R.id.FreatureListText);
            trt.setText(Html.fromHtml(getString(R.string.feature_list)));
         }

    }
}
