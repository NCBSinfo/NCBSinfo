package com.rohitsuratekar.NCBSinfo.activities.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;
import com.rohitsuratekar.NCBSinfo.background.RouteViewModel;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends LifecycleActivity {

    @BindView(R.id.home_time_text)
    TextView timeText;
    @BindView(R.id.home_image_back)
    ImageView backImage;
    @BindView(R.id.home_calender)
    TextView calenderText;

    private HomePageAdapter adapter;
    private RouteViewModel routeViewModel;
    private List<Fragment> fragmentList;
    private List<RouteInfo> infoList;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());

        routeViewModel = ViewModelProviders.of(this).get(RouteViewModel.class);
        adapter = new HomePageAdapter(getSupportFragmentManager(), fragmentList);
        ViewPager pager = ButterKnife.findById(this, R.id.home_viewpager);
        pager.setAdapter(adapter);
        addFragments();
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(backImage,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(15000);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentIndex = i;
                updateUI();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    /**
     * Converts values from animator into string and adds extra "0" if its single digit
     *
     * @param number : number object
     * @return : balanced string
     */
    private String digitConverter(Object number) {
        if ((int) number < 10) {
            return "0" + String.valueOf(number);
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * Opens option menu
     *
     * @param view: imageView
     */

    @OnClick(R.id.home_option)
    public void showOption(View view) {
        PopupMenu popup = new PopupMenu(Home.this, view);
        popup.getMenuInflater().inflate(R.menu.home_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_show_trips:
                        showAllTrips();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    /**
     * Starts Transport activity
     */
    @OnClick({R.id.home_show_all, R.id.home_show_all_text})
    public void showAllTrips() {
        startActivity(new Intent(this, Transport.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Changes the favorite route to current route
     */
    @OnClick(R.id.home_favorite)
    public void changeFavorite() {

    }

    private void addFragments() {

        routeViewModel.getAllRoutes().observe(this, new Observer<List<RouteInfo>>() {
            @Override
            public void onChanged(@Nullable List<RouteInfo> infos) {
                if (infos != null) {
                    Log.inform("Routes loaded successfully");
                    infoList = infos;
                    fragmentList.clear();
                    for (RouteInfo r : infos) {
                        fragmentList.add(HomeFragment.newInstance(
                                r.getRoute().getOrigin(),
                                r.getRoute().getDestination(),
                                r.getRoute().getType()));
                    }
                    adapter.notifyDataSetChanged();
                    updateUI();
                }
            }
        });

    }

    private void updateUI() {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, infoList.get(currentIndex).getMinute());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                timeText.setText(getString(R.string.home_time,
                        digitConverter(infoList.get(currentIndex).getHour()),
                        digitConverter(animation.getAnimatedValue()),
                        infoList.get(currentIndex).getAmPm()));

            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(300);
        animator.start();
        Calendar calendar = Calendar.getInstance();
        if (infoList.get(currentIndex).getDay() > 0) {
            calendar.add(Calendar.DATE, 1);
        }
        calenderText.setText(getString(R.string.home_date,
                DateConverter.convertToString(calendar, "EEEE, dd MMM")));
    }


}
