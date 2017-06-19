package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.secretbiology.helpers.general.Log;

import butterknife.ButterKnife;

public class Contacts extends BaseActivity implements AppCompatCallback {

    private AppCompatDelegate delegate;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        setTitle(R.string.contacts);
        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setHomeButtonEnabled(true);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);


    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_contacts;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        delegate.getMenuInflater().inflate(R.menu.contact_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.getIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchSrcTextView.setThreshold(1);
            // searchSrcTextView.setAdapter(suggestionAdapter);

            searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    searchView.clearFocus();
                    MenuItemCompat.collapseActionView(searchItem);
                    searchView.setIconified(true);
                    final String i = ((TextView) view).getText().toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //openSheet(originalItems.indexOf(i), true);
                        }
                    }, 200);


                }
            });
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.inform(newText);
//                    suggestionItems.clear();
//                    for (String s : originalItems) {
//                        if (s.toLowerCase().contains(newText.toLowerCase())) {
//                            suggestionItems.add(s);
//                        }
//                    }
//                    suggestionAdapter.notifyDataSetChanged();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    MenuItemCompat.collapseActionView(searchItem);
                    searchView.setIconified(true);
//                    for (final String s : originalItems) {
//                        if (s.toLowerCase().contains(query.toLowerCase())) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    openSheet(originalItems.indexOf(s), true);
//                                }
//                            }, 200);
//
//                        }
//                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        searchView.setOnQueryTextListener(queryTextListener);
        if (item.getItemId() == R.id.action_search) {
            onSearchRequested();
            // bottomSheet.dismissSheet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
