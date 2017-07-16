package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 16-07-17 for NCBSinfo.
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
        View rootView = inflater.inflate(R.layout.transport_sheet, container, false);

        if (getActivity() instanceof Transport && getArguments() != null) {
            List<RouteData> routeDataList = ((Transport) getActivity()).getRouteDataList();
            RecyclerView recyclerView = ButterKnife.findById(rootView, R.id.tp_sheet_recycler);
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


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnRouteSelected) context;
        } catch (Exception e) {
            General.makeLongToast(context, "attach fragment interface!");
        }

    }

    interface OnRouteSelected {
        void selected(int routeID);
    }
}