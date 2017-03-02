package com.rohitsuratekar.NCBSinfo.activities.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Intro extends AppCompatActivity {

    @BindView(R.id.intro_viewpager)
    ViewPager pager;
    @BindView(R.id.intro_button)
    FloatingActionButton button;
    @BindView(R.id.intro_text)
    TextView introText;

    private ViewpagerAdapter adapter;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        ButterKnife.bind(this);
        //Sign out all existing users
        FirebaseAuth.getInstance().signOut();
        setupViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (position == adapter.getCount() - 1) {
                    button.setImageResource(R.drawable.icon_done);
                    introText.setText("Lets go!");
                } else {
                    button.setImageResource(R.drawable.icon_right_arrow);
                    introText.setText(getString(R.string.next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < adapter.getCount() - 1) {
                    currentPage++;
                    pager.setCurrentItem(currentPage, true);
                } else {
                    new AppPrefs(getBaseContext()).introSeen();
                    Intent intent = new Intent(Intro.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.image_inapp_logo, R.string.app_name, R.string.intro_tip1, false), "General1");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.icon_favorite, R.string.intro_tip2, R.string.intro_tip2_details, true), "General2");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.icon_transport, R.string.intro_tip3, R.string.intro_tip3_details, true), "General3");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.icon_sync, R.string.intro_tip4, R.string.intro_tip4_details, true), "General4");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.icon_developer, R.string.intro_tip5, R.string.intro_tip5_details, true), "General5");
        viewPager.setAdapter(adapter);
    }
}
