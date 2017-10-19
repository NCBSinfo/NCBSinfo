package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contacts extends BaseActivity {

    @BindView(R.id.ct_recycler)
    RecyclerView recyclerView;

    private List<ContactModel> modelList;
    private List<ContactModel> originalList;
    private SuggestionAdapter suggestionAdapter;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        setTitle(R.string.contacts);
        ButterKnife.bind(this);
        modelList = new ContactList().getAll();
        originalList = new ArrayList<>(modelList);
        adapter = new ContactAdapter(modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ct_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.getIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }

        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_contact));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            SearchView.SearchAutoComplete searchSrcTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchSrcTextView.setThreshold(1);


            searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    //  outsideHeader.setText("");
                    // contactListRecycler.setAdapter(searchAdapter);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    // contactListRecycler.setAdapter(listAdapter);
                }
            });

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    modelList.clear();
                    for (ContactModel c : originalList) {
                        c.clearArea();
                        if (c.getName().toLowerCase().contains(newText.toLowerCase())) {
                            c.addSearchArea(ContactModel.AREA.NAME);
                        }
                        if (c.getPrimaryExtension().toLowerCase().contains(newText.toLowerCase())) {
                            c.addSearchArea(ContactModel.AREA.EXTENSION);
                        }
                        if (c.getDetails().toLowerCase().contains(newText.toLowerCase())) {
                            c.addSearchArea(ContactModel.AREA.DETAILS);
                        }
                        if (c.getSearchArea().size() > 0) {
                            c.setSearchString(newText);
                            modelList.add(c);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
//                    if (allContacts.size() > 0) {
//                        showDetails(allContacts.get(0));
//                    }
                    searchView.clearFocus();
                    MenuItemCompat.collapseActionView(searchItem);
                    searchView.setIconified(true);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_contacts;
    }
}
