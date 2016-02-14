package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dexter on 1/30/2016.
 */
public class ContactTab2 extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText inputSearch;
    private ArrayList<DataObject> resultsFav = new ArrayList<DataObject>();
    ArrayList<DataObject> mAllData = new ArrayList<DataObject>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED = "com.rohitsuratekar.test1.RADIO_DATASET_CHANGED";
    private Radio radio;

    public ContactTab2() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        radio = new Radio();

        View rootView2 = inflater.inflate(R.layout.contact_tab1, container, false);
        mRecyclerView = (RecyclerView) rootView2.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter2 = new ContactAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter2);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView2);
        Boolean Firstvalue = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("firstTime2", true);

        if (Firstvalue){
            DBHandler db = new DBHandler(getContext());
            String[][] clist = new contactList().allContacts();
            for (int i=0; i <clist.length; i++){
                db.addContact(new SQfields(1, clist[i][0], clist[i][1],clist[i][2], clist[i][3], "0"));
            }
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("firstTime2", false).apply();
            db.close();
        }


        searchBt = (ImageButton) rootView2.findViewById(R.id.searchButton);
        searchBt.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                if (toggleSearchCancel) {
                    searchBt.setImageResource(R.drawable.ic_highlight_remove_24dp);
                    toggleSearchCancel = false;
                    //inputSearch.setInputType(1);
                } else {
                    hideSoftKeyboard(getActivity());
                    searchBt.setImageResource(R.drawable.ic_search_24dp);
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

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

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
                intent.setData(Uri.parse("tel:" + resultsFav.get(position).getmText2()));
                startActivity(intent);

            }
        });
        ((ContactAdapter) mAdapter2).setOnItemClickListener2(new ContactAdapter.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.fav_button);

                DBHandler db2 = new DBHandler(getContext());
                SQfields doc = db2.getDocument(resultsFav.get(position).getmID());
                if (doc.getContactFavorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setContactFavorite("1");
                    mAdapter2.notifyDataSetChanged();
                } else {
                    img.setColorFilter(null);
                    doc.setContactFavorite("0");
                    ((ContactAdapter) mAdapter2).deleteItem(position);
                    mAdapter2.notifyItemRemoved(position);
                    Intent intent = new Intent(ContactTab1.RADIO_DATASET_CHANGED2); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }


                db2.updateDocument(doc);
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
        }catch (Exception e){
            //Cannot unregister receiver
        }

    }

    private ArrayList<DataObject> getDataSet() {



        DBHandler db2 = new DBHandler(getContext());
        List<SQfields> documents = db2.getAllDocuments();

        for (SQfields cn : documents) {
            if (cn.getContactFavorite().equals("1")) {
                resultsFav.add(new DataObject(cn.getContactName(), cn.getContactExtension(), cn.getContactDepartment(), cn.getContactID()));
            }
        }

        mAllData.addAll(resultsFav);
        db2.close();
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
                searchBt.setImageResource(R.drawable.ic_highlight_remove_24dp);
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
            for (DataObject wp : mAllData) {
                if (wp.getmText1().toLowerCase(Locale.getDefault()).contains(charText) || wp.getmText2().toLowerCase(Locale.getDefault()).contains(charText)) {
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

            searchBt.setImageResource(R.drawable.ic_search_24dp);
            toggleSearchCancel = true;
            resultsFav.clear();
            resultsFav.addAll(mAllData);
            mAdapter2.notifyDataSetChanged();
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
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class Radio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED)){
                //Change received from previous fragment
                refresh();
                mAdapter2.notifyDataSetChanged();
            }
        }
    }

    public void refresh(){
        resultsFav.clear();

        DBHandler db2 = new DBHandler(getContext());
        List<SQfields> documents = db2.getAllDocuments();

        for (SQfields cn : documents) {
            if (cn.getContactFavorite().equals("1")) {
                resultsFav.add(new DataObject(cn.getContactName(), cn.getContactExtension(), cn.getContactDepartment(), cn.getContactID()));
            }
        }
        mAllData.clear();
        mAllData.addAll(resultsFav);
        db2.close();
        mAdapter2.notifyDataSetChanged();

    }
}

