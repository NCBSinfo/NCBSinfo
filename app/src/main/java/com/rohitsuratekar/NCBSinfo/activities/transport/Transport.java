package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends BaseActivity {

    @BindView(R.id.tp_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.tp_recycler_right)
    RecyclerView recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        ButterKnife.bind(this);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.transport);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new TransportAdapter(3, 9));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView2.setAdapter(new TransportAdapter(-1, 8));
    }

    @Override
    protected int setNavigationMenu() {
        return 0;
    }

    @OnClick(R.id.tp_show_routes)
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new TransportFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
