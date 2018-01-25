package com.rohitsuratekar.NCBSinfo.activities.home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohitsuratekar.NCBSinfo.BuildConfig;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Dashboard;
import com.rohitsuratekar.NCBSinfo.activities.intro.Intro;
import com.rohitsuratekar.NCBSinfo.activities.locations.Locations;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.activities.transport.ManageTransport;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportFragment;
import com.rohitsuratekar.NCBSinfo.background.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.background.services.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;
import com.secretbiology.helpers.general.listeners.OnSwipeTouchListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements TransportFragment.OnRouteSelected, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.hm_image)
    ImageView imageView;
    @BindView(R.id.hm_time)
    TextView time;
    @BindView(R.id.hm_type)
    TextView type;
    @BindView(R.id.hm_route)
    TextView route;
    @BindView(R.id.hm_main_layout)
    ConstraintLayout mainLayout;
    @BindView(R.id.hm_fav)
    ImageView favorite;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_navigation_view)
    NavigationView navigationView;

    @BindViews({R.id.hm_sug1, R.id.hm_sug2})
    List<TextView> suggestionViews;
    List<Integer> suggestions = new ArrayList<>();

    @BindViews({R.id.progressBar, R.id.hm_loading_image, R.id.hm_loading_text})
    List<View> loadingViews;

    private HomeObject currentObject;
    private boolean isDirectionRight = true;
    private AppPrefs prefs;
    private FirebaseAnalytics mFirebaseAnalytics;
    private HomeViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this);
        changeHeader();

        //New test for custom events for analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_opened", Helper.timestamp());
        mFirebaseAnalytics.logEvent("home", params);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getHomeObject().observe(this, new Observer<HomeObject>() {
            @Override
            public void onChanged(@Nullable HomeObject homeObject) {
                if (homeObject != null) {
                    currentObject = homeObject;
                    updateUI(homeObject);
                    for (View v : loadingViews) {
                        v.setVisibility(View.INVISIBLE);
                    }
                    mainLayout.setVisibility(View.VISIBLE);
                    if (!prefs.isIntroSeen()) {
                        startActivity(new Intent(Home.this, Intro.class));
                        animateTransition();
                    }
                }
            }
        });

        viewModel.getCreateDefault().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        Log.i(getClass().getSimpleName(), "No routes found. Creating default ones.");
                        new CreateDefaultRoutes(getBaseContext(), new OnFinish() {
                            @Override
                            public void finished() {
                                viewModel.startCalculations(getApplicationContext(), false);
                            }

                            @Override
                            public void allRoutes(List<RouteData> routeDataList) {

                            }
                        }).execute();
                    }
                }
            }
        });

        mainLayout.setVisibility(View.INVISIBLE);
        prefs = new AppPrefs(getApplicationContext());
        prefs.appOpened();
        for (View v : loadingViews) {
            v.setVisibility(View.VISIBLE);
        }
        Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse);
        findViewById(R.id.hm_show_all).startAnimation(pulse);

        if (prefs.isFirstTime()) {
            cancelOldAlarms();
            transferOldPrefs();
        } else {
            // Only start calculation if there is no model.
            // This will keep state in changes in orientation
            if (viewModel.getHomeObject().getValue() == null) {
                if (prefs.isSpecialNoticeActivated() && !prefs.isSpecialNoticeSeen()) {
                    //Update NCBS-ICTS from version 56
                    viewModel.updateNCBS_ICTS(getApplicationContext());
                    prefs.specialNoticeSeen();
                    //TODO: Implement update broadcast properly for updating routes
                } else {
                    viewModel.startCalculations(getApplicationContext(), false);
                }
            }
        }
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            protected void onSwipeRight() {
                currentObject.goNext();
                updateUI(currentObject);
            }

            @Override
            protected void onSwipeLeft() {
                isDirectionRight = false;
                currentObject.goBack();
                updateUI(currentObject);
            }

            @Override
            protected void onSwipeTop() {
                showBottomSheet();
            }

            @Override
            protected void onSwipeBottom() {
                gotoTransport();
            }

        });


    }

    private void updateUI(HomeObject object) {
        route.setText(getString(R.string.tp_route_name, object.getOrigin().toUpperCase(), object.getDestination().toUpperCase()));
        type.setText(getString(R.string.tp_next_transport, object.getType()));
        if (currentObject.getFavoriteRoute() == object.getRouteNo()) {
            favorite.setImageResource(R.drawable.icon_favorite);
            mFirebaseAnalytics.setUserProperty("favorite_route", currentObject.getOrigin() + "-" + currentObject.getDestination() + " " + currentObject.getType());
        } else {
            favorite.setImageResource(R.drawable.icon_favorite_outline);
        }
        try {
            time.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, object.getNextTrip().calculate(Calendar.getInstance())[0], "hh:mm a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        suggestions.clear();

        suggestionViews.get(0).setVisibility(View.GONE);
        suggestionViews.get(1).setVisibility(View.GONE);

        if (object.getRelatedRoutes().size() > 0) {
            suggestionViews.get(0).setVisibility(View.VISIBLE);
            suggestionViews.get(0).setText(convertToReadable(object.getRouteData().get(object.getRelatedRoutes().get(0))));
            suggestions.add(object.getRelatedRoutes().get(0));
        }

        if (object.getRelatedRoutes().size() > 1) {
            suggestionViews.get(1).setVisibility(View.VISIBLE);
            suggestionViews.get(1).setText(convertToReadable(object.getRouteData().get(object.getRelatedRoutes().get(1))));
            suggestions.add(object.getRelatedRoutes().get(1));
        }

        Bitmap newImg = BitmapFactory.decodeResource(getResources(), getImage(object.getDestination()));
        ImageViewAnimatedChange(getApplicationContext(), imageView, newImg);

    }

    @OnClick(R.id.hm_sug1)
    public void checkSugg1() {
        if (suggestions.get(0) != null) {
            currentObject.setRoute(suggestions.get(0));
            updateUI(currentObject);
        } else {
            General.makeShortToast(getApplicationContext(), "Something is wrong!");
        }
    }

    @OnClick(R.id.hm_sug2)
    public void checkSugg2() {
        if (suggestions.get(1) != null) {
            currentObject.setRoute(suggestions.get(1));
            updateUI(currentObject);
        } else {
            General.makeShortToast(getApplicationContext(), "Something is wrong!");
        }
    }

    private String convertToReadable(RouteData r) {
        return getString(R.string.hm_full_route_name, r.getOrigin().toUpperCase(), r.getDestination().toUpperCase(), r.getType());
    }

    @OnClick(R.id.hm_see_all_holder)
    public void gotoTransport() {
        Intent intent = new Intent(Home.this, Transport.class);
        intent.putExtra(Transport.ROUTE, currentObject.getRouteNo());
        startActivity(intent);
        animateTransition();
    }

    @OnClick(R.id.hm_fav)
    public void setFavorite() {
        CommonTasks.sendFavoriteRoute(getApplicationContext(), currentObject.getRouteNo());
        currentObject.setFavoriteRoute(currentObject.getRouteNo());
        favorite.setImageResource(R.drawable.icon_favorite);
        Snackbar snackbar = Snackbar.make(mainLayout, "Default route changed!", BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        snackbar.show();
    }


    @OnClick({R.id.hm_route, R.id.hm_sug3})
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment;
        if (currentObject != null) {
            bottomSheetDialogFragment = TransportFragment.newInstance(currentObject.getRouteNo(), -1);
        } else {
            bottomSheetDialogFragment = TransportFragment.newInstance(-1, -1);
        }
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnClick(R.id.hm_nav_icon)
    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.START);
    }


    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out, anim_in;

        if (isDirectionRight) {
            anim_out = AnimationUtils.loadAnimation(c, android.R.anim.slide_out_right);
            anim_in = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        } else {
            anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_left);
            anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_right);
            isDirectionRight = true;
        }

        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    private int getImage(String destination) {
        switch (destination) {
            case "ncbs":
                return R.drawable.ncbs;
            case "iisc":
                return R.drawable.iisc;
            case "icts":
                return R.drawable.icts;
            default:
                return R.drawable.unknown;
        }
    }


    @Override
    public void selected(int routeID) {
        currentObject.setRoute(routeID);
        updateUI(currentObject);
    }

    public HomeObject getCurrentObject() {
        return currentObject;
    }


    private void animateTransition() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void transferOldPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String origin = sharedPreferences.getString("n1_favorite_origin", "ncbs");
        String destination = sharedPreferences.getString("n1_favorite_destination", "iisc");
        String type = sharedPreferences.getString("n1_favorite_type", "shuttle");
        prefs.clear();
        prefs.setFavoriteOrigin(origin);
        prefs.setFavoriteDestination(destination);
        prefs.setFavoriteType(type);
        prefs.appOpened();
        prefs.appMigrated();
        prefs.appOpenedFirstTime();
        viewModel.startCalculations(getApplicationContext(), true);
    }

    private void cancelOldAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int[] oldAlarms = new int[]{2000, 2003, 2004, 2005, 2006, 1000, 1001, 1989, 1990, 1991, 1992};
        Intent intent = new Intent(getApplicationContext(), Alarms.class);
        for (int id : oldAlarms) {
            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_NO_CREATE);
            if (sender != null && alarmManager != null) {
                alarmManager.cancel(sender);
            }
        }
        Log.i(getClass().getSimpleName(), "Cancelled all past alarms!");
    }


    @OnClick(R.id.hm_footnote)
    public void egg() {
        startActivity(new Intent(this, HomeEgg.class));
        animateTransition();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            animateTransition();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        final Activity activity = this;
        if (activity.getClass() != getNavClass(item.getItemId())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (item.getItemId() != R.id.nav_transport) {
                        startActivity(new Intent(activity, getNavClass(item.getItemId())));
                        animateTransition();
                    } else {
                        gotoTransport();
                    }
                }
            }, 300);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_home);
        if (item != null) {
            item.setChecked(true);
        }
    }

    /**
     * Useful for navigating between activities.
     *
     * @param navID : Navigation ID
     * @return : Class to change the activity
     */
    private Class getNavClass(int navID) {
        switch (navID) {
            case R.id.nav_home:
                return Home.class;
            case R.id.nav_transport:
                return Transport.class;
            case R.id.nav_contacts:
                return Contacts.class;
            case R.id.nav_settings:
                return Settings.class;
            case R.id.nav_dash:
                return Dashboard.class;
            case R.id.nav_location:
                return Locations.class;
            case R.id.nav_manage_transport:
                return ManageTransport.class;
        }
        return Home.class;
    }

    /**
     * Change header color and gradient.
     * All header titles and clicks can be handled from here.
     */
    private void changeHeader() {
        View header = navigationView.getHeaderView(0);
        GradientDrawable backgroundGradient = (GradientDrawable) header.getBackground();
        backgroundGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        backgroundGradient.setGradientCenter(10, 0);
        backgroundGradient.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        AppPrefs prefs = new AppPrefs(getApplicationContext());
        TextView headerTitle = header.findViewById(R.id.nav_header_title);
        TextView headerSubTitle = header.findViewById(R.id.nav_header_subtitle);
        if (headerTitle != null && headerSubTitle != null) {
            if (prefs.isUsedLoggedIn()) {
                headerTitle.setText(prefs.getUserName());
                headerSubTitle.setText(prefs.getUserEmail());
            } else {
                int versionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;
                headerSubTitle.setText(getString(R.string.header_subtitle, versionCode, versionName));
            }
        }

    }
}
