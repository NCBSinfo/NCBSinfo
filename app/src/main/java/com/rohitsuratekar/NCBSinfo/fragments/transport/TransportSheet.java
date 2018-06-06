package com.rohitsuratekar.NCBSinfo.fragments.transport;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.OnRouteSelect;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.ArrayList;
import java.util.List;

public class TransportSheet extends BottomSheetDialogFragment {

    private static String INDEX = "index";
    private static String REVERSE = "reverseIndex";

    public static TransportSheet newInstance(int index, int reverseIndex) {
        TransportSheet myFragment = new TransportSheet();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        args.putInt(REVERSE, reverseIndex);
        myFragment.setArguments(args);
        return myFragment;
    }

    private OnRouteSelect selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.transport_sheet, container, false);

        if (getActivity() != null) {
            List<RouteData> routeDataList = new ArrayList<>();
            List<TransportDetails> objectList = ((BaseActivity) getActivity()).getTransportList();
            if (objectList != null) {
                for (TransportDetails t : objectList) {
                    routeDataList.add(t.getRouteData());
                }
            }
            setRecycler(rootView, routeDataList);
        }


        return rootView;
    }

    private void setRecycler(View rootView, List<RouteData> routeDataList) {
        RecyclerView recyclerView = rootView.findViewById(R.id.tp_sheet_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        TransportSheetAdapter adapter = new TransportSheetAdapter(routeDataList, getArguments().getInt(INDEX, 0), getArguments().getInt(REVERSE, 0));
        recyclerView.setAdapter(adapter);
        adapter.setOnRouteClick(new TransportSheetAdapter.onRouteClick() {
            @Override
            public void clicked(int routeID) {
                dismiss();
                selected.routeSelected(routeID);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnRouteSelect) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }

}
