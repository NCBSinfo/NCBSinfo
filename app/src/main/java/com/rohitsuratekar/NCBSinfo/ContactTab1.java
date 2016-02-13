package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Dexter on 1/30/2016.
 */
public class ContactTab1 extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView;
    FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    EditText inputSearch;
    private ArrayList<DataObject> results = new ArrayList<DataObject>();
    ArrayList<DataObject> mAllData = new ArrayList<DataObject>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED2 = "com.rohitsuratekar.test1.RADIO_DATASET_CHANGED2";
    private Radio radio;



    public ContactTab1() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        radio = new Radio();
        View rootView = inflater.inflate(R.layout.contact_tab1, container, false);


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ContactAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView);
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


        searchBt = (ImageButton) rootView.findViewById(R.id.searchButton);
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
        ((ContactAdapter) mAdapter).setOnItemClickListener(new ContactAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                hideSoftKeyboard(getActivity());
                ((ContactAdapter) mAdapter).setPosition(results.get(position).getmID());
                v.showContextMenu();
                //Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:" + results.get(position).getmText2()));
                //startActivity(intent);

            }
        });
        ((ContactAdapter) mAdapter).setOnItemClickListener2(new ContactAdapter.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.fav_button);

                DBHandler db2 = new DBHandler(getContext());
                SQfields doc = db2.getDocument(results.get(position).getmID());
                if (doc.getContactFavorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setContactFavorite("1");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(ContactTab2.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                } else {
                    img.setColorFilter(null);
                    doc.setContactFavorite("0");
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(ContactTab2.RADIO_DATASET_CHANGED); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }


                db2.updateDocument(doc);
                db2.close();
            }
        });


    }

    private ArrayList<DataObject> getDataSet() {



        DBHandler db2 = new DBHandler(getContext());
        List<SQfields> documents = db2.getAllDocuments();

        for (SQfields cn : documents) {
            results.add(new DataObject(cn.getContactName(), cn.getContactExtension(), cn.getContactID()));
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
                searchBt.setImageResource(R.drawable.ic_highlight_remove_24dp);
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
            for (DataObject wp : mAllData) {
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
            searchBt.setImageResource(R.drawable.ic_search_24dp);
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

        DBHandler db2 = new DBHandler(getContext());
        List<SQfields> documents = db2.getAllDocuments();

        for (SQfields cn : documents) {
            results.add(new DataObject(cn.getContactName(), cn.getContactExtension(), cn.getContactID()));
            if (cn.getContactFavorite().equals("0")){
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
        final SQfields doc;
        final DBHandler db2 = new DBHandler(getContext());

        try {
            position = ((ContactAdapter) mAdapter).getPosition();
            doc = db2.getDocument(((int) position));
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        if(item.getItemId() == R.id.contact_list_call){

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + doc.getContactExtension()));
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.contact_list_delete){
            final long finalPosition = position;
            new AlertDialog.Builder(getActivity())
                    .setTitle("Are you sure?")
                    .setMessage("You are abount to delete "+doc.getContactName()+" from this list.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            db2.deleteDocument(doc);

                            getActivity().finish();
                            getContext().startActivity(new Intent(getContext(), ContactActivity.class));
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

        if(item.getItemId() == R.id.contact_list_fav){

            if (doc.getContactFavorite().equals("0")) {
                doc.setContactFavorite("1");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(ContactTab2.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            } else {
                doc.setContactFavorite("0");
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(ContactTab2.RADIO_DATASET_CHANGED); //This will update fragment
                getActivity().getApplicationContext().sendBroadcast(intent);
            }

            db2.updateDocument(doc);
        }



        else{
            return false;
        }

    db2.close();
    return super.onContextItemSelected(item);
    }

}