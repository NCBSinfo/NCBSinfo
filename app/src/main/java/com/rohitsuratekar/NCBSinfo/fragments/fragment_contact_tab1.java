package com.rohitsuratekar.NCBSinfo.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.Activity_AddContact;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Contact;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_contact;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_divideritemdecoratio;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_list;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_database;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_row;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class fragment_contact_tab1 extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView;
    FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText inputSearch;
    private ArrayList<models_contacts_row> results = new ArrayList<models_contacts_row>();
    ArrayList<models_contacts_row> mAllData = new ArrayList<models_contacts_row>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED2 = "com.rohitsuratekar.NCBSinfo.RADIO_DATASET_CHANGED2";
    private Radio radio;



    public fragment_contact_tab1() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        radio = new Radio();
        View rootView = inflater.inflate(R.layout.activity_contact_tab, container, false);


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_contact);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new adapters_contact(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new helper_contact_divideritemdecoratio(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView);
        Boolean Firstvalue = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("firstTime2", true);

        if (Firstvalue){
            DatabaseHelper db = new DatabaseHelper(getContext());
            String[][] clist = new helper_contact_list().allContacts();
            for (int i=0; i <clist.length; i++){
                db.addContact(new models_contacts_database(1, clist[i][0], clist[i][1],clist[i][2], clist[i][3], "0"));
            }
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("firstTime2", false).apply();
            db.close();
        }


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

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((adapters_contact) mAdapter).setOnItemClickListener(new adapters_contact.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                hideSoftKeyboard(getActivity());
                ((adapters_contact) mAdapter).setPosition(results.get(position).getmID());
                v.showContextMenu();
                //Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:" + results.get(position).getmText2()));
                //startActivity(intent);

            }
        });
        ((adapters_contact) mAdapter).setOnItemClickListener2(new adapters_contact.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.fav_button);

                DatabaseHelper db2 = new DatabaseHelper(getContext());
                models_contacts_database doc = db2.getContact(results.get(position).getmID());
                if (doc.getContact_favorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setContact_favorite("1");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(fragment_contact_tab2.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                } else {
                    img.setColorFilter(null);
                    doc.setContact_favorite("0");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(fragment_contact_tab2.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }


                db2.updateContact(doc);
                db2.close();
            }
        });


    }

    private ArrayList<models_contacts_row> getDataSet() {



        DatabaseHelper db2 = new DatabaseHelper(getContext());
        List<models_contacts_database> contacts = db2.getAllContacts();

        for (models_contacts_database cn : contacts) {
            results.add(new models_contacts_row(cn.getContact_name(), cn.getContact_extension(), cn.getContact_department(), cn.getContact_id()));
        }

        mAllData.addAll(results);
        db2.close();
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
            for (models_contacts_row wp : mAllData) {
                if (wp.getmText1().toLowerCase(Locale.getDefault()).contains(charText) || wp.getmText2().toLowerCase(Locale.getDefault()).contains(charText)) {
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
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class Radio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED2)){
                //Change received from previous fragment
                refresh();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refresh(){
        results.clear();

        DatabaseHelper db2 = new DatabaseHelper(getContext());
        List<models_contacts_database> contacts = db2.getAllContacts();

        for (models_contacts_database cn : contacts) {
            results.add(new models_contacts_row(cn.getContact_name(), cn.getContact_extension(), cn.getContact_department(), cn.getContact_id()));
            if (cn.getContact_favorite().equals("0")){
                ImageButton img = (ImageButton) rootView.findViewById(R.id.fav_button);
                img.setColorFilter(null);
            }
        }
        mAllData.clear();
        mAllData.addAll(results);
        db2.close();
        mAdapter.notifyDataSetChanged();

    }

    public boolean onContextItemSelected(MenuItem item) {
        long position = -1;
        final models_contacts_database doc, doc2;
        final DatabaseHelper db2 = new DatabaseHelper(getContext());

        try {
            position = ((adapters_contact) mAdapter).getPosition();
            doc = db2.getContact(((int) position));
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        if(item.getItemId() == R.id.contact_list_call){

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + doc.getContact_extension()));
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.contact_list_delete){
            final long finalPosition = position;
            new AlertDialog.Builder(getActivity())
                    .setTitle("Are you sure?")
                    .setMessage("You are abount to delete "+doc.getContact_name()+" from this list.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            db2.deleteContact(doc);

                            getActivity().finish();
                            getContext().startActivity(new Intent(getContext(), Activity_Contact.class));
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
        }

        else if(item.getItemId() == R.id.contact_list_fav){

            if (doc.getContact_favorite().equals("0")) {
                doc.setContact_favorite("1");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(fragment_contact_tab2.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            } else {
                doc.setContact_favorite("0");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(fragment_contact_tab2.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            }

            db2.updateContact(doc);
        }

        else if(item.getItemId() == R.id.contact_list_edit){
            position = ((adapters_contact) mAdapter).getPosition();
            doc2 = db2.getContact(((int) position));
            //TODO
            //Add contact activity
            Intent intent = new Intent(getContext(), Activity_AddContact.class);
            intent.putExtra("forEdit", 1);
            intent.putExtra("feldID", doc2.getContact_id() );
            startActivity(intent);

        }



        else{
            return false;
        }

        db2.close();
        return super.onContextItemSelected(item);
    }

}