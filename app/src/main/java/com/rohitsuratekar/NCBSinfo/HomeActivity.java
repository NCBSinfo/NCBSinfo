package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener,
        OnShowcaseEventListener, NavigationView.OnNavigationItemSelectedListener {

    private Timer TimeLeft;
    String GlobalShuttleFrom = "ncbs";
    String GlobalShuttleto = "iisc";
    int isBuggy = 0;
    int Scrollint = 0;
    Calendar c = Calendar.getInstance();
    int currentDay = c.get(Calendar.DAY_OF_WEEK);
    int currentMonth = c.get(Calendar.MONTH);
    int currentYear = c.get(Calendar.YEAR);
    float currentHour = c.get(Calendar.HOUR_OF_DAY);
    float currentMinute = c.get(Calendar.MINUTE);
    int currentSecond = c.get(Calendar.SECOND);
    int currentDate = c.get(Calendar.DATE);
    Date d1 =   new Date();
    int currentItem=0;
    ShowcaseView showcaseview;
    String guideTitle = "";
    String guideDetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        setTitle(R.string.title_activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Scrollint==0 || Scrollint==1 ){
                Intent intent = new Intent(HomeActivity.this, NCBStoIISC.class);
                    intent.putExtra("switch",String.valueOf(Scrollint));
                startActivity(intent);
                }
                else if (Scrollint>1 && Scrollint<6){
                    Intent intent = new Intent(HomeActivity.this, NCBStoMandara.class);
                    intent.putExtra("switch",String.valueOf(Scrollint));
                    startActivity(intent);
                }
                else if (Scrollint==6){
                    Intent intent = new Intent(HomeActivity.this, NCBStoOther.class);
                    intent.putExtra("switch",String.valueOf(0));
                    startActivity(intent);
                }
                else if (Scrollint==7){
                    Intent intent = new Intent(HomeActivity.this, NCBStoOther.class);
                    intent.putExtra("switch",String.valueOf(1));
                    startActivity(intent);
                }
                else if (Scrollint==8){
                    Intent intent = new Intent(HomeActivity.this, NCBStoOther.class);
                    intent.putExtra("switch",String.valueOf(2));
                    startActivity(intent);
                }
            }
        });
        TimeLeft = new Timer();
        TimeLeft.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000); //1000

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initial Conditions
        //Get all saved parameters

        String shuttleRoute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shuttle_route_list", "0");
        String[] place = new ExtraFunctions().getRouteName(Integer.valueOf(shuttleRoute));

        GlobalShuttleFrom = place[0];
        GlobalShuttleto = place[1];
        isBuggy = Integer.parseInt(place[2]);

        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        TextView home_currentime = (TextView)findViewById(R.id.currentDateText);
        home_currentime.setText(Html.fromHtml(getString(R.string.home_current_datetime, format1.format(d1), format2.format(d1))));



        NumberPicker picker = (NumberPicker)findViewById(R.id.numberPicker);
        String[] values = getResources().getStringArray(R.array.route_names);
        picker.setMinValue(0);
        picker.setMaxValue(values.length - 1);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues(values);
        picker.setOnValueChangedListener(new WheelListener());
        picker.setValue(new ExtraFunctions().RouteNo(GlobalShuttleFrom, GlobalShuttleto, isBuggy));
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        //First Time Guide
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("guideSeen2", true))
        {
            RelativeLayout main = (RelativeLayout)findViewById(R.id.MainLayout);
            RelativeLayout welcome = (RelativeLayout)findViewById(R.id.WelcomeLayout);
            main.setVisibility(View.GONE);
            welcome.setVisibility(View.VISIBLE);
            guideTitle = getString(R.string.guide_title1);
            guideDetails = getString(R.string.guide_details1);
            viewcase(R.id.firstTarget);
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("guideSeen", false).apply();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

        if (currentItem==1){
            RelativeLayout main = (RelativeLayout)findViewById(R.id.MainLayout);
            RelativeLayout welcome = (RelativeLayout)findViewById(R.id.WelcomeLayout);
            guideTitle= getString(R.string.guide_title2);
            guideDetails = getString(R.string.guide_details2);
            main.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);

            viewcase(R.id.numberPicker);
        }
        else if (currentItem==2){
            guideTitle= getString(R.string.guide_title3);
            guideDetails = getString(R.string.guide_details3);
            viewcase(R.id.NextShuttleTime);
        }
        else if (currentItem==3){
            guideTitle= getString(R.string.guide_title5);
            guideDetails = getString(R.string.guide_details5);
            viewcase(R.id.TimerAnimation);
        }
        else if (currentItem==4){
            guideTitle= getString(R.string.guide_title5);
            guideDetails = getString(R.string.guide_details5);
            viewcase(R.id.fab);

        }
        else if (currentItem==5){
            guideTitle= getString(R.string.guide_title6);
            guideDetails = getString(R.string.guide_details6);
            viewcase(R.id.first_time_demo);

        }

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    private class WheelListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //get new value and convert it to String
            //if you want to use variable value elsewhere, declare it as a field
            //of your main function
            String value = "" + newVal;
            switch (value) {
                case "0":
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "iisc";
                    isBuggy = 0;
                    Scrollint = 0;
                    break;
                case "1":
                    GlobalShuttleFrom = "iisc";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    Scrollint = 1;
                    break;
                case "2":
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "mandara";
                    isBuggy = 0;
                    Scrollint = 2;
                    break;
                case "3":
                    GlobalShuttleFrom = "mandara";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    Scrollint = 3;
                    break;
                case "4":
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "mandara";
                    isBuggy = 1;
                    Scrollint = 4;
                    break;
                case "5":
                    GlobalShuttleFrom = "mandara";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 1;
                    Scrollint = 5;
                    break;
                case "6":
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "icts";
                    isBuggy = 0;
                    Scrollint = 6;
                    break;
                case "7":
                    GlobalShuttleFrom = "icts";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    Scrollint = 7;
                    break;
                case "8":
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "cbl";
                    isBuggy = 0;
                    Scrollint = 8;
                    break;
            }

            setTextinField();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent;
            intent = new Intent( HomeActivity.this, SettingsActivity.class );
            intent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.DefaultShuttleFragment.class.getName() );
            intent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_full_shuttle) {

            if (Scrollint==0 || Scrollint==1 ){
                Intent intent = new Intent(HomeActivity.this, NCBStoIISC.class);
                intent.putExtra("switch",String.valueOf(Scrollint));
                startActivity(intent);
            }
            else if (Scrollint>1 && Scrollint<6){
                Intent intent = new Intent(HomeActivity.this, NCBStoMandara.class);
                intent.putExtra("switch",String.valueOf(Scrollint));
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(HomeActivity.this, NCBStoIISC.class);
                intent.putExtra("switch",String.valueOf(0));
                startActivity(intent);

            }

        } else if (id == R.id.nav_settings) {
            Intent intent;
            intent = new Intent( HomeActivity.this, SettingsActivity.class );
            intent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.DefaultShuttleFragment.class.getName() );
            intent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent;
            intent = new Intent( HomeActivity.this, ContactActivity.class );
            startActivity(intent);

        } else if (id == R.id.nav_full_buggy){
            Intent intent = new Intent(HomeActivity.this, NCBStoMandara.class);
            intent.putExtra("switch",String.valueOf(4));
            startActivity(intent);

        } else if (id == R.id.nav_other){
            Intent intent = new Intent( HomeActivity.this, NCBStoOther.class);
            intent.putExtra("switch","0");
            startActivity(intent);
            return true;
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            setTextinField();
        }
    };

    public void setTextinField() {

        TextView home_next = (TextView)findViewById(R.id.home_next_shuttle);
        String tempText;
        if (isBuggy==1){tempText=getString(R.string.next_buggy);}
        else {tempText=getString(R.string.next_shuttle);}
        home_next.setText(tempText);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        TextView nextShuttle2 = (TextView)findViewById(R.id.NextShuttleTime);
        Calendar nextDate = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        nextShuttle2.setText(dformat.format(nextDate.getTime()));

        float[] Difference = new ExtraFunctions().DateTimeDifferentExample(format.format(c2.getTime()), format.format(nextDate.getTime()));

        LinearLayout ln = (LinearLayout) findViewById(R.id.SecondBarFill);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ln.getLayoutParams();
        params.weight = Difference[0];
        ln.setLayoutParams(params);

        LinearLayout ln2 = (LinearLayout) findViewById(R.id.MinuteBarFill);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ln2.getLayoutParams();
        params2.weight = Difference[1];
        ln2.setLayoutParams(params2);

        LinearLayout ln3 = (LinearLayout) findViewById(R.id.HourBarFill);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) ln3.getLayoutParams();
        params3.weight = Difference[2];
        ln3.setLayoutParams(params3);

        TextView t1 = (TextView)findViewById(R.id.HourLeft);
        TextView t2 = (TextView)findViewById(R.id.MinuteLeft);
        TextView t3 = (TextView)findViewById(R.id.SecondLeft);
        t1.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.hour_left, Math.round(Difference[2]), Math.round(Difference[2]))));
        t2.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.min_left, Math.round(Difference[1]), Math.round(Difference[1]))));
        t3.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.sec_left, Math.round(Difference[0]), Math.round(Difference[0]))));

        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        TextView home_currentime = (TextView)findViewById(R.id.currentDateText);
        home_currentime.setText(Html.fromHtml(getString(R.string.home_current_datetime, format1.format(d1), format2.format(d1))));

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
