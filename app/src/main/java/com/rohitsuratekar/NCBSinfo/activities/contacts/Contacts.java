package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contacts extends BaseActivity implements ContactFragment.OnContactSelected {

    @BindView(R.id.ct_recycler)
    RecyclerView recyclerView;

    private List<ContactModel> modelList;
    private List<ContactModel> originalList;
    private SearchView searchView = null;
    private ContactAdapter adapter;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        setTitle(R.string.contacts);
        ButterKnife.bind(this);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        modelList = new ContactList().getAll();
        sortList(modelList);
        originalList = new ArrayList<>(modelList);
        adapter = new ContactAdapter(modelList, new ContactAdapter.OnContactClick() {
            @Override
            public void clicked(int position) {
                showBottomSheet(modelList.get(position));
                searchView.clearFocus();
                searchItem.collapseActionView();
                searchView.setIconified(true);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    public void showBottomSheet(ContactModel model) {
        BottomSheetDialogFragment bottomSheetDialogFragment = ContactFragment.newInstance(model);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ct_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null && sortItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.getIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
            sortItem.getIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }

        if (searchView != null && searchManager != null) {
            searchView.setQueryHint(getString(R.string.search_contact));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
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
                    searchView.clearFocus();
                    searchItem.collapseActionView();
                    searchView.setIconified(true);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    private void sortList(List<ContactModel> list) {
        List<ContactModel> tempList1 = new ArrayList<>();
        List<ContactModel> tempList2 = new ArrayList<>();
        for (ContactModel m : list) {
            if (m.getType().toLowerCase().trim().equals("imp")) {
                tempList1.add(m);
            } else {
                tempList2.add(m);
            }
        }
        list.clear();
        list.addAll(tempList1);
        list.addAll(tempList2);
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_contacts;
    }


    @Override
    public void onCalled(String call) {
        Log.inform(call);
        //TODO : Add permission access for calling, Feedback dialog
    }
}
