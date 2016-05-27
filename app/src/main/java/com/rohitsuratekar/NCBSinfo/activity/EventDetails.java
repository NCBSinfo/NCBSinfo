package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_updates_details);

        Intent intent = getIntent();
        String datacode = intent.getStringExtra(General.GEN_EVENTDETAILS_DATACODE);
        int dataID = intent.getExtras().getInt(General.GEN_EVENTDETAILS_DATA_ID, 1);
        Database db = new Database(getBaseContext());
        TextView common1 = (TextView)findViewById(R.id.details_CommonElement1);
        TextView common2 = (TextView)findViewById(R.id.details_CommonElement2);

        if (datacode.equals(General.GEN_DATACODE_CBJC)||datacode.equals(General.GEN_DATACODE_DBJC)) {
            List<DataModel> list = db.getFullDatabase();
            if(list.size()>0){
            final DataModel info = db.getDatabaseEntry(dataID);

            RelativeLayout jc = (RelativeLayout) findViewById(R.id.JC_Layout);
            TextView JCname = (TextView) findViewById(R.id.SD_jc_title);
            TextView title = (TextView) findViewById(R.id.SD_title);
            TextView date = (TextView) findViewById(R.id.SD_date);
            TextView speaker = (TextView) findViewById(R.id.SD_speaker);
            TextView time = (TextView) findViewById(R.id.SD_time);
            TextView venue = (TextView) findViewById(R.id.SD_venue);
            TextView nextspeaker = (TextView) findViewById(R.id.SD_nextspeaker);
            TextView url = (TextView) findViewById(R.id.SD_url);
                common1.setText("Next Speaker");
                common2.setText("Abstract");

            if (info.getDate() != null) {
                Date dt = new GeneralHelp().convertToDate(info.getDate(), info.getTime());
                DateFormat currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                DateFormat currentTime = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());
                date.setText(currentDate.format(dt));
                time.setText(currentTime.format(dt));
                JCname.setText(info.getDatacode());
                title.setText("\"" + info.getTalkabstract() + "\"");
                speaker.setText(info.getSpeaker());
                venue.setText(info.getVenue());
                nextspeaker.setText(info.getNextspeaker());
                if(info.getNextspeaker().length()<1){
                    common1.setVisibility(View.GONE);
                }
                url.setTextColor(Color.BLUE);
            } else {
                JCname.setText("No record found");
                date.setText("N/A");
                time.setText("N/A");
                title.setText("Something went wrong");
                speaker.setText("N/A");
                venue.setText("N/A");
                nextspeaker.setText("N/A");
            }

            Intent i = null;
            if (info.getUrl().length() != 0) {
                String currenturl = info.getUrl();
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currenturl));

            }
            final Intent finalI = i;
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getUrl().length() != 0) {
                        startActivity(finalI);
                    }
                }
            });
            }
        }
        else if (datacode.equals(General.GEN_DATACODE_TALK)) {
            List<TalkModel> list = db.getTalkDatabase();

            if (list.size() > 0) {

                final TalkModel info = db.getTalkDataEntry(dataID);

                TextView JCname = (TextView) findViewById(R.id.SD_jc_title);
                TextView title = (TextView) findViewById(R.id.SD_title);
                TextView date = (TextView) findViewById(R.id.SD_date);
                TextView speaker = (TextView) findViewById(R.id.SD_speaker);
                TextView time = (TextView) findViewById(R.id.SD_time);
                TextView venue = (TextView) findViewById(R.id.SD_venue);
                TextView nextspeaker = (TextView) findViewById(R.id.SD_nextspeaker);
                TextView url = (TextView) findViewById(R.id.SD_url);
                common1.setText("Affiliation");
                common2.setText("Host");

                if (info.getDate() != null) {
                    Date dt = new GeneralHelp().convertToDate(info.getDate(), info.getTime());
                    DateFormat currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                    DateFormat currentTime = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());
                    date.setText(currentDate.format(dt));
                    time.setText(currentTime.format(dt));
                    JCname.setText("Research Talk");
                    title.setText("\"" + info.getTitle() + "\"");
                    speaker.setText(info.getSpeaker());
                    venue.setText(info.getVenue());
                    nextspeaker.setText(info.getAffilication());
                    url.setTextColor(venue.getCurrentHintTextColor());
                    url.setText(info.getHost());
                } else {
                    JCname.setText("No record found");
                    date.setText("N/A");
                    time.setText("N/A");
                    title.setText("Something went wrong");
                    speaker.setText("N/A");
                    venue.setText("N/A");
                    nextspeaker.setText("N/A");
                    url.setText("N/A");
                }


            }
        }
   }
}
