package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class ContactListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView;
    FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText inputSearch;
    private ArrayList<ContactRowModel> results = new ArrayList<ContactRowModel>();
    ArrayList<ContactRowModel> mAllData = new ArrayList<ContactRowModel>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED2 = "com.rohitsuratekar.NCBSinfo.RADIO_DATASET_CHANGED2";
    private Radio radio;
    SharedPreferences pref;


    public ContactListFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        radio = new Radio();
        View rootView = inflater.inflate(R.layout.contact_tab, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        fab = (FloatingActionButton) rootView.findViewById(R.id.base_fab_button);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contacts_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ContactAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView);

        searchBt = (ImageButton) rootView.findViewById(R.id.searchButton);
        searchBt.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                if (toggleSearchCancel) {
                    searchBt.setImageResource(R.drawable.icon_close);
                    toggleSearchCancel = false;
                    //inputSearch.setInputType(1);
                } else {
                    hideSoftKeyboard(getActivity());
                    searchBt.setImageResource(R.drawable.icon_search);
                    toggleSearchCancel = true;
                    results.clear();
                    results.addAll(mAllData);
                    mAdapter.notifyDataSetChanged();
                    // inputSearch.setInputType(0);
                    inputSearch.setText("");
                }
            }
        });

        inputSearch = (EditText) rootView.findViewById(R.id.searchTextBox);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ContactAdapter) mAdapter).setOnItemClickListener(new ContactAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                hideSoftKeyboard(getActivity());
                ((ContactAdapter) mAdapter).setPosition(results.get(position).getId());
                v.showContextMenu();
            }
        });
        ((ContactAdapter) mAdapter).setOnItemClickListener2(new ContactAdapter.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.contact_fav_button);

                ContactModel doc = new ContactsData(getContext()).get(results.get(position).getId());
                if (doc.getFavorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setFavorite("1");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(FavoriteContactsFragment.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                } else {
                    img.setColorFilter(null);
                    doc.setFavorite("0");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(FavoriteContactsFragment.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }

                new ContactsData(getContext()).update(doc);

            }
        });


    }

    private ArrayList<ContactRowModel> getDataSet() {

        List<ContactModel> contacts = new ContactsData(getContext()).getAll();
        for (ContactModel cn : contacts) {
            results.add(new ContactRowModel(cn.getName(), cn.getExtension(), cn.getDepartment(), cn.getId()));
        }

        //For alphabetical sorting
        /*Collections.sort(results, new Comparator<ContactRowModel>() {
            @Override
            public int compare(ContactRowModel lhs, ContactRowModel rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });*/

        mAllData.addAll(results);
        return results;
    }

    private void doSearch(View v) {
        final EditText et = (EditText) v.findViewById(R.id.searchTextBox);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBt.setImageResource(R.drawable.icon_close);
                toggleSearchCancel = false;
                String text = et.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        results.clear();

        if (charText.length() == 0) {
            results.addAll(mAllData);
        } else {
            for (ContactRowModel wp : mAllData) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getNuber().toLowerCase(Locale.getDefault()).contains(charText)) {
                    results.add(wp);
                }
            }

        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            fab.setVisibility(View.GONE);

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {

            fab.setVisibility(View.VISIBLE);
            searchBt.setImageResource(R.drawable.icon_search);
            toggleSearchCancel = true;
            results.clear();
            results.addAll(mAllData);
            mAdapter.notifyDataSetChanged();
            //inputSearch.setInputType(0);
            inputSearch.setText("");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class Radio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED2)) {
                //Change received from previous fragment
                refresh();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refresh() {
        results.clear();
        List<ContactModel> contacts = new ContactsData(getContext()).getAll();

        for (ContactModel cn : contacts) {
            results.add(new ContactRowModel(cn.getName(), cn.getExtension(), cn.getDepartment(), cn.getId()));
            if (cn.getFavorite().equals("0")) {
                ImageButton img = (ImageButton) rootView.findViewById(R.id.contact_fav_button);
                img.setColorFilter(null);
            }
        }
        mAllData.clear();
        mAllData.addAll(results);
        mAdapter.notifyDataSetChanged();

    }

    public boolean onContextItemSelected(MenuItem item) {
        long position = -1;
        final ContactModel doc, doc2;

        try {
            position = ((ContactAdapter) mAdapter).getPosition();
            doc = new ContactsData(getContext()).get(((int) position));
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        if (item.getItemId() == R.id.contact_list_call) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + doc.getExtension()));
            startActivity(intent);
        } else if (item.getItemId() == R.id.contact_list_delete) {
            final long finalPosition = position;
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.warning_are_you_sure))
                    .setMessage(getResources().getString(R.string.warning_delete_contact, doc.getName()))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            new ContactsData(getContext()).delete(doc);
                            getActivity().finish();
                            getContext().startActivity(new Intent(getContext(), Contacts.class));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return false;
        } else if (item.getItemId() == R.id.contact_list_fav) {

            if (doc.getFavorite().equals("0")) {
                doc.setFavorite("1");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(FavoriteContactsFragment.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            } else {
                doc.setFavorite("0");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(FavoriteContactsFragment.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            }

            new ContactsData(getContext()).update(doc);
        } else if (item.getItemId() == R.id.contact_list_edit) {
            position = ((ContactAdapter) mAdapter).getPosition();
            doc2 = new ContactsData(getContext()).get(((int) position));

            Intent intent = new Intent(getContext(), ContactAdd.class);
            intent.putExtra("forEdit", 1);
            intent.putExtra("feldID", doc2.getId());
            startActivity(intent);

        } else {
            return false;
        }

        return super.onContextItemSelected(item);
    }

}