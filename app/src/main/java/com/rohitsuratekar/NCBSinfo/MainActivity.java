package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnShowcaseEventListener {
    DecoView arcView;
    int series3Index, series2Index, series1Index, isBuggy;
    int currentIndex =0;
    String[] ShuttleStringList;
    String GlobalShuttleFrom, GlobalShuttleto;
    int currentItem=0;
    ShowcaseView showcaseview;
    String guideTitle = "";
    String guideDetails = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For contact activity
        Boolean Firstvalue = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("firstTime2", true);

        if (Firstvalue){
            DBHandler db = new DBHandler(getBaseContext());
            String[][] clist = new contactList().allContacts();
            for (int i=0; i <clist.length; i++){
                db.addContact(new SQfields(1, clist[i][0], clist[i][1],clist[i][2], clist[i][3], "0"));
            }
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("firstTime2", false).apply();
            db.close();
        }

        //Set locale
        String langtvalue = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("default_language", "0");
        String languageToLoad  = new ExtraFunctions().currentLang(langtvalue); // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        arcView= (DecoView)findViewById(R.id.dynamicArcView);
        ShuttleStringList = getResources().getStringArray(R.array.route_names);

        // Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(5f)
                .build());

        //Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#ff5722")) //second line
                .setRange(0, 60, 0)
                .setLineWidth(16f)
                .setInitialVisibility(true)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#4caf50")) //minute line
                .setRange(0, 60, 0)
                .setLineWidth(16f)
                .setInset(new PointF(16f, 16f))
                .setInitialVisibility(true)
                .build();

        SeriesItem seriesItem3 = new SeriesItem.Builder(Color.parseColor("#3f51b5")) //Hour line
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
        arcView.addEvent(new DecoEvent.Builder(c2.get(Calendar.SECOND)).setIndex(series1Index).setDuration(500).build());

        final TextView hTextView = (TextView) findViewById(R.id.text);
        currentIndex = new ExtraFunctions().RouteNo(GlobalShuttleFrom, GlobalShuttleto, isBuggy);
        hTextView.setText(ShuttleStringList[currentIndex]); // animate

        String tempText;
        if (isBuggy==1){tempText=getString(R.string.next_buggy);}
        else {tempText=getString(R.string.next_shuttle);}
        TextView nameT = (TextView)findViewById(R.id.HomePageText);
        nameT.setText(tempText);
        Date d1 =   new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        TextView home_currentime = (TextView)findViewById(R.id.main_screen_timetext);
        home_currentime.setText(Html.fromHtml(getString(R.string.home_current_datetime2, format2.format(d1))));

        hTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, hTextView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.main_screen_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.main_popup_r0) { currentIndex = -1; goNext();
                        } else if (item.getItemId() == R.id.main_popup_r1) { currentIndex = 0; goNext();
                        } else if (item.getItemId() == R.id.main_popup_r2) { currentIndex = 1; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r3) { currentIndex = 2; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r4) { currentIndex = 3; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r5) { currentIndex = 4; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r6) { currentIndex = 5; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r7) { currentIndex = 6; goNext();
                        }else if (item.getItemId() == R.id.main_popup_r8) { currentIndex = 7; goNext();
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("guideSeen", true))
        {
            guideTitle = getString(R.string.guide_title1);
            guideDetails = getString(R.string.guide_details1);
            viewcase(R.id.dynamicArcView);
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("guideSeen", false).apply();
        }

    }



    public void showNext(View arg0){
        goNext();
    }

    public void showPrevious(View arg0){
        goPrevious();
    }

    public void gotoShuttle(View arg0){
        Intent intent = new Intent(MainActivity.this, AllShuttles.class);
        intent.putExtra("switch", String.valueOf(currentIndex));
        startActivity(intent);
    }

    public void gotoContact(View arg0){
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
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

            goPrevious();

        }

        public void onLeftSwipe() {
            goNext();

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
        arcView.addEvent(new DecoEvent.Builder(c2.get(Calendar.SECOND)).setIndex(series1Index).setDuration(500).build());

        String tempText;
        if (isBuggy==1){tempText=getString(R.string.next_buggy);}
        else {tempText=getString(R.string.next_shuttle);}
        TextView nameT = (TextView)findViewById(R.id.HomePageText);
        nameT.setText(tempText);

        Date d1 =   new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        TextView home_currentime = (TextView)findViewById(R.id.main_screen_timetext);
        home_currentime.setText(Html.fromHtml(getString(R.string.home_current_datetime2, format2.format(d1))));

    }

    public void goNext(){
        currentIndex++;
        if (currentIndex>=ShuttleStringList.length){
            currentIndex = 0;
        }

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
        arcView.addEvent(new DecoEvent.Builder(c2.get(Calendar.SECOND)).setIndex(series1Index).setDuration(500).build());

        String tempText;
        if (isBuggy==1){tempText=getString(R.string.next_buggy);}
        else {tempText=getString(R.string.next_shuttle);}
        TextView nameT = (TextView)findViewById(R.id.HomePageText);
        nameT.setText(tempText);

        Date d1 =   new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        TextView home_currentime = (TextView)findViewById(R.id.main_screen_timetext);
        home_currentime.setText(Html.fromHtml(getString(R.string.home_current_datetime2, format2.format(d1))));
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

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

        if (currentItem==1){

            guideTitle= getString(R.string.guide_title2);
            guideDetails = getString(R.string.guide_details2);
            viewcase(R.id.text);
        }
        else if (currentItem==2){
            guideTitle= getString(R.string.guide_title3);
            guideDetails = getString(R.string.guide_details3);
            viewcase(R.id.dynamicArcView);
        }
        else if (currentItem==3){
            guideTitle= getString(R.string.guide_title5);
            guideDetails = getString(R.string.guide_details5);
            viewcase(R.id.ShuttleImageButton);
        }
        else if (currentItem==4){
            guideTitle= getString(R.string.guide_title6);
            guideDetails = getString(R.string.guide_details6);
            viewcase(R.id.ContactImageButton);

        }
        else if (currentItem==5){
            guideTitle= getString(R.string.guide_title7);
            guideDetails = getString(R.string.guide_details7);
            viewcase(R.id.Home_translation_button);

        }

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    public void viewcase(int viewID){
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        ViewTarget target = new ViewTarget(viewID, this);

        showcaseview = new ShowcaseView.Builder(this)
                .hideOnTouchOutside()
                .setTarget(target)
                .setContentTitle(guideTitle)
                .setContentText(guideDetails)
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(this)
                .withHoloShowcase()
                .replaceEndButton(R.layout.view_custom_button)
                .build();

        showcaseview.setButtonPosition(lps);
        currentItem = currentItem+1;
    }

}
