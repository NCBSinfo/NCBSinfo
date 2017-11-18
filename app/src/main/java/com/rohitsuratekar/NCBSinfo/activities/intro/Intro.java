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
                    // new AppPrefs(getBaseContext()).introSeen();
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
                R.drawable.intro1, R.string.intro_text1_heading, R.string.intro_text1_details, false), "General1");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.intro2, R.string.intro_text2_heading, R.string.intro_text2_details, false), "General2");
        adapter.addFragment(GeneralIntroFragment.newInstance(
                R.drawable.intro3, R.string.intro_text3_heading, R.string.intro_text3_details, false), "General3");
        viewPager.setAdapter(adapter);
    }
}
