package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class FavoriteContactsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText inputSearch;
    private ArrayList<ContactRowModel> resultsFav = new ArrayList<ContactRowModel>();
    ArrayList<ContactRowModel> mAllData = new ArrayList<ContactRowModel>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED = "com.rohitsuratekar.NCBSinfo.RADIO_DATASET_CHANGED";
    private Radio radio;

    public FavoriteContactsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        radio = new Radio();

        View rootView2 = inflater.inflate(R.layout.contact_tab, container, false);
        mRecyclerView = (RecyclerView) rootView2.findViewById(R.id.contacts_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter2 = new ContactAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter2);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView2);

        searchBt = (ImageButton) rootView2.findViewById(R.id.searchButton);
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
                    resultsFav.clear();
                    resultsFav.addAll(mAllData);
                    mAdapter2.notifyDataSetChanged();
                    // inputSearch.setInputType(0);
                    inputSearch.setText("");

                }
            }
        });

        inputSearch = (EditText) rootView2.findViewById(R.id.searchTextBox);

        return rootView2;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ContactAdapter) mAdapter2).setOnItemClickListener(new ContactAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                hideSoftKeyboard(getActivity());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + resultsFav.get(position).getNuber()));
                startActivity(intent);

            }
        });
        ((ContactAdapter) mAdapter2).setOnItemClickListener2(new ContactAdapter.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.contact_fav_button);

                ContactModel doc = new ContactsData(getContext()).get(resultsFav.get(position).getId());
                if (doc.getFavorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setFavorite("1");
                    mAdapter2.notifyDataSetChanged();
                } else {
                    img.setColorFilter(null);
                    doc.setFavorite("0");
                    ((ContactAdapter) mAdapter2).deleteItem(position);
                    mAdapter2.notifyItemRemoved(position);
                    Intent intent = new Intent(ContactListFragment.RADIO_DATASET_CHANGED2); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }


                new ContactsData(getContext()).update(doc);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(RADIO_DATASET_CHANGED);
        getActivity().getApplicationContext().registerReceiver(radio, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().getApplicationContext().unregisterReceiver(radio);
        } catch (Exception e) {
            //Cannot unregister receiver
        }

    }

    private ArrayList<ContactRowModel> getDataSet() {


        List<ContactModel> contacts = new ContactsData(getContext()).getAll();

        for (ContactModel cn : contacts) {
            if (cn.getFavorite().equals("1")) {
                resultsFav.add(new ContactRowModel(cn.getName(), cn.getExtension(), cn.getDepartment(), cn.getId()));
            }
        }

        mAllData.addAll(resultsFav);
        return resultsFav;
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
        resultsFav.clear();

        if (charText.length() == 0) {
            resultsFav.addAll(mAllData);
        } else {
            for (ContactRowModel wp : mAllData) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getNuber().toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultsFav.add(wp);
                }
            }

        }
        mAdapter2.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {

            searchBt.setImageResource(R.drawable.icon_search);
            toggleSearchCancel = true;
            resultsFav.clear();
            resultsFav.addAll(mAllData);
            mAdapter2.notifyDataSetChanged();
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
            if (intent.getAction().equals(RADIO_DATASET_CHANGED)) {
                //Change received from previous fragment
                refresh();
                mAdapter2.notifyDataSetChanged();
            }
        }
    }

    public void refresh() {
        resultsFav.clear();

        List<ContactModel> contacts = new ContactsData(getContext()).getAll();

        for (ContactModel cn : contacts) {
            if (cn.getFavorite().equals("1")) {
                resultsFav.add(new ContactRowModel(cn.getName(), cn.getExtension(), cn.getDepartment(), cn.getId()));
            }
        }
        mAllData.clear();
        mAllData.addAll(resultsFav);
        mAdapter2.notifyDataSetChanged();

    }
}