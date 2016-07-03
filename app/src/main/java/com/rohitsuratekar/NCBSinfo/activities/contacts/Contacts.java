package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

public class Contacts extends BaseActivity {

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.CONTACTS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Contacts.this, ContactAdd.class);
                intent.putExtra("forEdit", 0);
                intent.putExtra("feldID", 0);
                startActivity(intent);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImportantContactFragment(), "Important");
        adapter.addFragment(new PinnedContactsFragment(), "Sections");
        adapter.addFragment(new FavoriteContactsFragment(), "Favorites");
        adapter.addFragment(new ContactListFragment(), "All contacts");
        viewPager.setAdapter(adapter);
    }
}
