package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.home.HomeObject;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportFragment extends BottomSheetDialogFragment {

    private static String INDEX = "index";
    private static String REVERSE = "reverseIndex";

    public static TransportFragment newInstance(int index, int reverseIndex) {
        TransportFragment myFragment = new TransportFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        args.putInt(REVERSE, reverseIndex);
        myFragment.setArguments(args);
        return myFragment;
    }

    private OnRouteSelected selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.transport_sheet, container, false);

        //TODO  : Change the layout of bottom sheet

        if (getActivity() instanceof Transport && getArguments() != null) {
            List<RouteData> routeDataList = ((Transport) getActivity()).getRouteDataList();
            setRecycler(rootView, routeDataList);

        } else if (getActivity() instanceof Home && getArguments() != null) {
            List<RouteData> routeDataList = new ArrayList<>();
            HomeObject object = ((Home) getActivity()).getCurrentObject();
            for (int i = 0; i < object.getRouteData().size(); i++) {
                routeDataList.add(object.getRouteData().get(object.getRouteData().keyAt(i)));
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
                selected.selected(routeID);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnRouteSelected) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }

    public interface OnRouteSelected {
        void selected(int routeID);
    }
}