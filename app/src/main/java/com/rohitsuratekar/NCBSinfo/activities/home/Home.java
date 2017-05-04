package com.rohitsuratekar.NCBSinfo.activities.home;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.AppState;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.common.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import butterknife.ButterKnife;

public class Home extends BaseActivity implements HomeFragment.OnFragmentActions {

    private AppState state = AppState.getInstance();
    private ViewpagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
        TextView title = ButterKnife.findById(this, R.id.special_tool_bar_title);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.home);

        mSectionsPagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < state.getRouteList().size(); i++) {
            mSectionsPagerAdapter.addFragment(HomeFragment.newInstance(i), "Route");
        }

        mViewPager = ButterKnife.findById(this, R.id.home_fragment);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            changeColor(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.HOME;
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(this, Transport.class));
        animateTransition();
    }

    private void changeColor(int position, float positionOffset) {
        if (position < (mSectionsPagerAdapter.getCount() - 1)) {
            toolbar.setBackgroundColor((Integer) new ArgbEvaluator().evaluate(
                    positionOffset,
                    General.getColor(getBaseContext(), state.getRouteColors().get(position)[0]),
                    General.getColor(getBaseContext(), state.getRouteColors().get(position + 1)[0])
            ));
            changeStatusBarColor((Integer) new ArgbEvaluator().evaluate(
                    positionOffset,
                    General.getColor(getBaseContext(), state.getRouteColors().get(position)[1]),
                    General.getColor(getBaseContext(), state.getRouteColors().get(position + 1)[1])
            ));

        } else {
            toolbar.setBackgroundColor(
                    General.getColor(getBaseContext(), state.getRouteColors().get(state.getRouteColors().size() - 1)[0]));
            changeStatusBarColor(General.getColor(getBaseContext(), state.getRouteColors().get(state.getRouteColors().size() - 1)[1]));
        }
    }

    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }
    }

}
