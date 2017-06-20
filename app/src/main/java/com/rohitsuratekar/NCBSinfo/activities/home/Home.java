package com.rohitsuratekar.NCBSinfo.activities.home;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends LifecycleActivity {

    @BindView(R.id.hm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.hm_loader_text)
    TextView loaderText;

    private HomeViewModel viewModel;
    private List<HomeCardModel> cardModels = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        adapter = new HomeAdapter(cardModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        loaderText.setVisibility(View.VISIBLE);
        subscribe();

        adapter.setOnCardClick(new HomeAdapter.OnCardClick() {
            @Override
            public void onCardClick(int position) {
                startActivity(new Intent(Home.this, Transport.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onFavoriteClick(int position) {
                Log.inform("Fav");
            }
        });
    }

    private void subscribe() {
        viewModel.getCardList().observe(this, new Observer<List<HomeCardModel>>() {
            @Override
            public void onChanged(@Nullable List<HomeCardModel> newCardModels) {
                if (newCardModels != null) {
                    cardModels.clear();
                    loaderText.setVisibility(View.GONE);
                    cardModels.addAll(newCardModels);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
