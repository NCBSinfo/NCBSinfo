package com.rohitsuratekar.NCBSinfo.activities.home;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.background.CommonTasks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends LifecycleActivity {

    @BindView(R.id.hm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.hm_loader_text)
    TextView loaderText;
    @BindView(R.id.hm_loading_image)
    ImageView loaderImage;

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
        loaderImage.setVisibility(View.VISIBLE);
        subscribe();

        adapter.setOnCardClick(new HomeAdapter.OnCardClick() {
            @Override
            public void onCardClick(int position) {
                Intent intent = new Intent(Home.this, Transport.class);
                intent.putExtra(Transport.ROUTE, cardModels.get(position).getRouteID());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onFavoriteClick(int position) {
                CommonTasks.sendFavoriteRoute(getApplicationContext(), cardModels.get(position).getRouteID());
                adapter.setCurrentFav(position);
                adapter.notifyDataSetChanged();
                Snackbar snackbar = Snackbar.make(recyclerView, R.string.home_favorite_changed, BaseTransientBottomBar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                snackbar.show();
            }
        });
    }

    private void subscribe() {
        viewModel.getCardList().observe(this, new Observer<List<HomeCardModel>>() {
            @Override
            public void onChanged(@Nullable List<HomeCardModel> newCardModels) {
                if (newCardModels != null) {
                    cardModels.clear();
                    cardModels.addAll(newCardModels);
                    adapter.notifyDataSetChanged();
                    if (cardModels.size() > 0) {
                        loaderText.setVisibility(View.GONE);
                        loaderImage.setVisibility(View.GONE);
                    } else {
                        loaderText.setText(R.string.home_no_routes);
                    }
                }
            }
        });
    }
}
