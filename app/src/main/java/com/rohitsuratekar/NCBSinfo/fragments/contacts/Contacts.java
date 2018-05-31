package com.rohitsuratekar.NCBSinfo.fragments.contacts;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts extends Fragment {


    @BindView(R.id.contact_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ct_recycler)
    RecyclerView recyclerView;

    private List<ContactModel> modelList;
    private List<ContactModel> originalList;
    private SearchView searchView = null;
    private ContactAdapter adapter;
    private MenuItem searchItem;
    private int sortOrder = 0;

    private OnContactSelected ctInteraction;


    public Contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts, container, false);
        ButterKnife.bind(this, rootView);
        toolbar.inflateMenu(R.menu.contact_menu);
        createMenu();
        toolbar.setTitle(R.string.contacts);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ctInteraction = (OnContactSelected) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }


    public void showBottomSheet(ContactModel model) {
        ctInteraction.contactSelected(model);
    }


    public void createMenu() {
        Menu menu = toolbar.getMenu();
        searchItem = menu.findItem(R.id.action_search);
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null && sortItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
            sortItem.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        }

        if (searchView != null && searchManager != null) {
            searchView.setQueryHint(getString(R.string.search_contact));
            // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_sort) {
                    if (sortOrder == 0) {
                        item.setIcon(R.drawable.icon_sort_alphabetical);
                        item.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);

                        if (item.getItemId() == R.id.action_sort) {
                            Collections.sort(modelList, new Comparator<ContactModel>() {
                                @Override
                                public int compare(ContactModel o1, ContactModel o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                        }
                        sortOrder = 1;
                    } else if (sortOrder == 1) {
                        Collections.reverse(modelList);
                        sortOrder = 2;
                    } else {
                        item.setIcon(R.drawable.icon_sort);
                        item.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
                        modelList.clear();
                        modelList.addAll(new ContactList().getAll());
                        sortList(modelList);
                        sortOrder = 0;
                    }
                    adapter.notifyDataSetChanged();
                }
                return true;

            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            if (sortOrder == 0) {
                item.setIcon(R.drawable.icon_sort_alphabetical);
                item.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);

                if (item.getItemId() == R.id.action_sort) {
                    Collections.sort(modelList, new Comparator<ContactModel>() {
                        @Override
                        public int compare(ContactModel o1, ContactModel o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }
                sortOrder = 1;
            } else if (sortOrder == 1) {
                Collections.reverse(modelList);
                sortOrder = 2;
            } else {
                item.setIcon(R.drawable.icon_sort);
                item.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
                modelList.clear();
                modelList.addAll(new ContactList().getAll());
                sortList(modelList);
                sortOrder = 0;
            }
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);


    }

    public interface OnContactSelected {
        void contactSelected(ContactModel contact);
    }


}
