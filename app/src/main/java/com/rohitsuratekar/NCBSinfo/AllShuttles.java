package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AllShuttles extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ncbs_to_iisc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Intent intent = getIntent();
        String currentSwitch = intent.getExtras().getString("switch");

        int currentInt = Integer.parseInt(currentSwitch);
        if (currentInt>4){currentInt=currentInt-1;}  //Remove extra buggy pointer
        TabLayout.Tab tab = tabLayout.getTabAt(currentInt); tab.select();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_tabbed, menu);
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
            intent = new Intent( AllShuttles.this, SettingsActivity.class );
            intent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.DefaultShuttleFragment.class.getName() );
            intent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );
            startActivity(intent);
            return true;
        }
        else if (id == R.id.tab_ncbs_iisc){
            Intent intent = new Intent(AllShuttles.this, NCBStoIISC.class);
            intent.putExtra("switch","0");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.tab_ncbs_mandara){
            return true;
        }
        else if (id == R.id.tab_other){
            Intent intent = new Intent( AllShuttles.this, NCBStoOther.class);
            intent.putExtra("switch","0");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0: return BasicShuttle.newInstance(0);
                case 1 :return BasicShuttle.newInstance(1);
                case 2 :return BasicShuttle.newInstance(2);
                case 3: return BasicShuttle.newInstance(3);
                case 4 :return BasicBuggy.newInstance(4);
                case 5: return BasicOther.newInstance(0);
                case 6 :return BasicOther.newInstance(1);
                case 7 :return BasicOther.newInstance(2);
                default: return BasicShuttle.newInstance(0);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_title1);
                case 1:
                    return getString(R.string.tab_title2);
                case 2:
                    return getString(R.string.tab_title3);
                case 3:
                    return getString(R.string.tab_title4);
                case 4:
                    return getString(R.string.tab_title5);
                case 5:
                    return getString(R.string.tab_title6);
                case 6:
                    return getString(R.string.tab_title7);
                case 7:
                    return getString(R.string.tab_title8);

            }
            return null;
        }
    }
}
