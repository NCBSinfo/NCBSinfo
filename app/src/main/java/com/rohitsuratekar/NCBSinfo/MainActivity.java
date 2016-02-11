package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    DecoView arcView;
    int series3Index, series2Index, series1Index, isBuggy;
    int currentIndex =0;
    String[] ShuttleStringList;
    String GlobalShuttleFrom, GlobalShuttleto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         arcView= (DecoView)findViewById(R.id.dynamicArcView);
        ShuttleStringList = getResources().getStringArray(R.array.route_names);

        // Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(5f)
                .build());

        //Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 60, 0)
                .setLineWidth(16f)
                .setInitialVisibility(true)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 96, 0))
                .setRange(0, 60, 0)
                .setLineWidth(16f)
                .setInset(new PointF(16f, 16f))
                .setInitialVisibility(true)
                .build();

        SeriesItem seriesItem3 = new SeriesItem.Builder(Color.argb(125, 164, 96, 0))
                .setRange(0, 24, 0)
                .setLineWidth(16f)
                .setInset(new PointF(32f, 32f))
                .setInitialVisibility(true)
                .build();


        series1Index = arcView.addSeries(seriesItem1);
        series2Index = arcView.addSeries(seriesItem2);
        series3Index = arcView.addSeries(seriesItem3);



        //Blinking Images
        ImageButton b1 = (ImageButton) findViewById(R.id.home_nextshuttle_btn);
        ImageButton b2 = (ImageButton) findViewById(R.id.home_nextshuttle_btn2);
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(3000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        b1.startAnimation(animation);
        b2.startAnimation(animation);


        TextView hTextView = (TextView) findViewById(R.id.text);
        hTextView.setText(ShuttleStringList[currentIndex]); // animate

        final ImageButton button1 = (ImageButton) findViewById(R.id.Home_translation_button);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.popup_english) {
                            changeLang("0");
                        } else if (item.getItemId() == R.id.popup_marathi) {
                            changeLang("1");
                        } else if (item.getItemId() == R.id.popup_bengali) {
                            changeLang("2");
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        RelativeLayout ln = (RelativeLayout) findViewById(R.id.MainRelativeLayout);
        ln.setOnTouchListener(activitySwipeDetector);

        String shuttleRoute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shuttle_route_list", "0");
        Random randomGenerator = new Random();
        int randomInt1 = randomGenerator.nextInt(60);

        String[] place = new ExtraFunctions().getRouteName(Integer.valueOf(shuttleRoute));
        GlobalShuttleFrom = place[0];
        GlobalShuttleto = place[1];
        isBuggy = Integer.parseInt(place[2]);
        TextView main1 = (TextView)findViewById(R.id.NextShuttle_MainScreen);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        main1.setText(dformat.format(nextDate.getTime()));
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.HOUR_OF_DAY)).setIndex(series3Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.MINUTE)).setIndex(series2Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(randomInt1).setIndex(series1Index).setDuration(500).build());

    }




    public void showNext(View arg0){
        goNext();
    }

    public void showPrevious(View arg0){
        goPrevious();
    }

    public void gotoShuttle(View arg0){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void gotoSetting(View arg0){
        Intent intent;
        intent = new Intent( MainActivity.this, SettingsActivity.class );
        intent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.DefaultShuttleFragment.class.getName() );
        intent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );
        startActivity(intent);
    }

    //Detection of swipe event and take action
    public class ActivitySwipeDetector implements View.OnTouchListener {

        static final String logTag = "ActivitySwipeDetector";
        private Activity activity;
        static final int MIN_DISTANCE = 100;
        private float downX, downY, upX, upY, rawX1, rawY1;

        public ActivitySwipeDetector(Activity activity) {
            this.activity = activity;
        }

        public void onRightSwipe() {
            goNext();


        }

        public void onLeftSwipe() {
            goPrevious();

        }

        public void onDownSwipe() {
            goNext();
        }

        public void onUpSwipe() {
            goPrevious();
        }

        public void onClick1() {



        }

        public void onClick2() {


        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    rawX1 = event.getRawX();
                    rawY1 = event.getRawY();

                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    float width = size.x;
                    float height = size.y;
                    int icon = 0;
                    if (rawY1 > (height * 0.82) && rawY1 < (height * 0.95)) {
                        if (rawX1 < (width * 0.33) && rawX1 > (width * 0.1)) {
                            icon = 1;
                        } else if (rawX1 > (width * 0.66) && rawX1 < (width * 0.9)) {
                            icon = 3;
                        }
                    }


                    // swipe horizontal?
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // left or right
                            if (deltaX > 0) {
                                this.onRightSwipe();
                                return true;
                            }
                            if (deltaX < 0) {
                                this.onLeftSwipe();
                                return true;
                            }
                        } else {
                            // return false; // We don't consume the event
                            if (icon == 1) {
                                this.onClick1();
                            } else if (icon == 3) {
                                this.onClick2();
                            }
                            return true;
                        }
                    }
                    // swipe vertical?
                    else {
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                this.onDownSwipe();
                                return true;
                            }
                            if (deltaY > 0) {
                                this.onUpSwipe();
                                return true;
                            }
                        } else {
                            //return false; // We don't consume the event
                            if (icon == 1) {
                                this.onClick1();
                            } else if (icon == 3) {
                                this.onClick2();
                            }
                            return true;
                        }
                    }

                    return true;
                }
            }
            return false;
        }

        public Activity getActivity() {
            return activity;
        }
    }

    public void goPrevious(){
        currentIndex = currentIndex-1;
        if (currentIndex<0){
            currentIndex = ShuttleStringList.length - 1;
        }
        Random randomGenerator=new Random();
        int randomInt1 = randomGenerator.nextInt(60);

        String[] place = new ExtraFunctions().getRouteName(currentIndex);
        GlobalShuttleFrom = place[0];
        GlobalShuttleto = place[1];
        isBuggy = Integer.parseInt(place[2]);
        TextView main1 = (TextView)findViewById(R.id.NextShuttle_MainScreen);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        main1.setText(dformat.format(nextDate.getTime()));

        TextView hTextView = (TextView) findViewById(R.id.text);
        hTextView.setText(ShuttleStringList[currentIndex]); // animate
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.HOUR_OF_DAY)).setIndex(series3Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.MINUTE)).setIndex(series2Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(randomInt1).setIndex(series1Index).setDuration(500).build());

    }

    public void goNext(){
        currentIndex++;
        if (currentIndex>=ShuttleStringList.length){
            currentIndex = 0;
        }
        Random randomGenerator = new Random();
        int randomInt1 = randomGenerator.nextInt(60);

        String[] place = new ExtraFunctions().getRouteName(currentIndex);
        GlobalShuttleFrom = place[0];
        GlobalShuttleto = place[1];
        isBuggy = Integer.parseInt(place[2]);
        TextView main1 = (TextView)findViewById(R.id.NextShuttle_MainScreen);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        main1.setText(dformat.format(nextDate.getTime()));

        TextView hTextView = (TextView) findViewById(R.id.text);
        hTextView.setText(ShuttleStringList[currentIndex]); // animate
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.HOUR_OF_DAY)).setIndex(series3Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(nextDate.get(Calendar.MINUTE)).setIndex(series2Index).setDuration(500).build());
        arcView.addEvent(new DecoEvent.Builder(randomInt1).setIndex(series1Index).setDuration(500).build());
    }

    public void changeLang(String code){
        Locale locale2 = new Locale(new ExtraFunctions().currentLang(code));
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale2;
        res.updateConfiguration(conf, dm);
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("default_language", code).apply();
        Intent intend = new Intent(getBaseContext(), MainActivity.class);
        finish();
        startActivity(intend);
    }

}
