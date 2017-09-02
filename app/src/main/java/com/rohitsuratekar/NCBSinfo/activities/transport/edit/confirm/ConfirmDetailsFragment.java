package com.rohitsuratekar.NCBSinfo.activities.transport.edit.confirm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.CurrentStateModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportRecyclerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmDetailsFragment extends Fragment {

    @BindView(R.id.transport_confirm_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.transport_edit_confirm_details)
    TextView details;
    @BindView(R.id.transport_confirm_empty)
    TextView empty;

    private OnStateChanged changed;
    private List<TransportRecyclerItem> list;
    private ConfirmAdapter adapter;


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_edit_confirm, container, false);
        ButterKnife.bind(this, rootView);
        list = new ArrayList<>();

        adapter = new ConfirmAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ConfirmAdapter.onSelected() {
            @Override
            public void onItemSelected(int position) {
                clearSelected();
                list.get(position).setSelected(!list.get(position).isSelected());
                adapter.notifyDataSetChanged();
                changed.onTripSelected(list.get(position).getText());
            }
        });

        CurrentStateModel model = ((TransportEdit) getActivity()).getInfo();
        if (model.getRouteList() != null) {
            for (String s : model.getRouteList()) {
                list.add(new TransportRecyclerItem(s, false));
            }
        }

        if (list.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            details.setVisibility(View.INVISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
        }

        if (((TransportEdit) getActivity()).isForEdit()) {
            if (list.size() > 0) {
                list.get(0).setSelected(true);
                adapter.notifyDataSetChanged();
            }
        }


        return rootView;
    }

    private void clearSelected() {
        for (TransportRecyclerItem item : list) {
            item.setSelected(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            changed = (OnStateChanged) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        super.onAttach(context);

    }

    public void setList(List<TransportRecyclerItem> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            details.setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);
        } else {
            details.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    // Container Activity must implement this interface
    public interface OnStateChanged {
        void onTripSelected(String selected);
    }


}
