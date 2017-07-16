package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.R;

public class EditTransport extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private ETViewModel viewModel;
    private int currentFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transport);
        viewModel = ViewModelProviders.of(this).get(ETViewModel.class);
        attachViewModels();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    private void attachViewModels() {
        viewModel.getCurrentStep().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    integer = 0;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //Animation when fragment changes
                if (currentFragmentIndex > integer) {
                    transaction.setCustomAnimations(R.anim.fragment_pop_enter, R.anim.fragment_pop_exit, R.anim.fragment_enter, R.anim.fragment_exit);
                } else {
                    transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
                }
                Fragment fragment;
                switch (integer) {
                    case 1:
                        fragment = new ETTypeFragment();
                        break;
                    case 2:
                        fragment = new ETFrequencyFragment();
                        break;
                    case 3:
                        fragment = new ETTripsFragment();
                        break;
                    case 4:
                        fragment = new ETFirstTripFragment();
                        break;
                    case 5:
                        fragment = new ETConfirmFragment();
                        break;
                    default:
                        fragment = new ETBasicFragment();

                }
                transaction.replace(R.id.et_view_holder, fragment);
                transaction.commit();
                currentFragmentIndex = integer;
            }
        });
    }
}
