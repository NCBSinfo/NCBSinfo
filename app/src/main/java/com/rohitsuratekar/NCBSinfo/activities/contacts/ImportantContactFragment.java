package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.ui.ScrollUpRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class ImportantContactFragment extends Fragment {

    List<ContactRowModel> allData;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView2 = inflater.inflate(R.layout.base_animated_fragment, container, false);
        ScrollUpRecyclerView mRecyclerView = (ScrollUpRecyclerView) rootView2.findViewById(R.id.base_animated_recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[][] DataList = new ContactList().getEssentials();

        allData = new ArrayList<>();

        for (String[] cn : DataList) {
            allData.add(new ContactRowModel(cn[0], cn[3], cn[1], 1)); //Name, number, email, ID
        }

        PinnedContactAdapter mAdapter2 = new PinnedContactAdapter(allData);
        mRecyclerView.setAdapter(mAdapter2);
//        RecyclerView.ItemDecoration itemDecoration =
//                new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
//        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter2.setOnItemClickListener(new PinnedContactAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + allData.get(position).getNuber()));
                startActivity(intent);
            }
        });

        return rootView2;
    }


}
