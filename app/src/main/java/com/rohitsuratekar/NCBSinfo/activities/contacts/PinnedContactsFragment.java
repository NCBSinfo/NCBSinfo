package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class PinnedContactsFragment extends Fragment {

    List<ContactRowModel> allData;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView2 = inflater.inflate(R.layout.base_fragment, container, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView2.findViewById(R.id.base_recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[][] DataList = new ContactList().getPinned();

        allData= new ArrayList<>();

        for (String[] cn : DataList) {
            allData.add(new ContactRowModel(cn[0], cn[3], cn[1], 1)); //Name, number, email, ID
        }

        PinnedContactAdapter mAdapter2 = new PinnedContactAdapter(allData);
        mRecyclerView.setAdapter(mAdapter2);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter2.setOnItemClickListener(new PinnedContactAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("What do you want?")
                        .setMessage("Call "+ allData.get(position).getName() + " or copy email ?")
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + allData.get(position).getNuber()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Copy Email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied Text", allData.get(position).getDepartment());
                                clipboard.setPrimaryClip(clip);
                                String toastString = "Copied "+allData.get(position).getDepartment()+ " to clipboard";
                                Toast.makeText(getContext(),toastString,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

            }
        });

        return rootView2;
    }


}
