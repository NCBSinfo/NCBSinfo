package com.rohitsuratekar.NCBSinfo.activities.transport.edit.route;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.CurrentStateModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.secretbiology.helpers.general.views.InputView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddLocationFragment extends Fragment {

    @BindView(R.id.te_add_input_origin)
    InputView origin;
    @BindView(R.id.te_add_input_destination)
    InputView destination;
    @BindView(R.id.te_add_recycler)
    RecyclerView recyclerView;

    private OnStateChanged changed;
    private boolean originAdded;
    private boolean destinationAdded;
    private List<LocationSuggestions> suggestionsList;


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_edit_location, container, false);
        ButterKnife.bind(this, rootView);

        suggestionsList = new ArrayList<>();
        List<Route> routeList = CurrentSession.getInstance().getAllRoutes();

        for (Route r : routeList) {
            suggestionsList.add(new LocationSuggestions(r.getOrigin(), r.getDestination()));
        }


        LocationSuggestionAdapter adapter = new LocationSuggestionAdapter(suggestionsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //Need following line to remove custom text instance
        origin.getEditText().setSaveEnabled(false);
        destination.getEditText().setSaveEnabled(false);

        CurrentStateModel model = ((TransportEdit) getActivity()).getInfo();

        if (model.getDestination() != null) {
            destination.setText(model.getDestination());
        }
        if (model.getOrigin() != null) {
            origin.setText(model.getOrigin());
        }

        origin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    originAdded = true;
                    if (destinationAdded) {
                        changed.onLocationChanged(origin.getText(), destination.getText());
                    }
                } else {
                    originAdded = false;
                    changed.onLocationRemoved(origin.getText(), destination.getText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destination.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    destinationAdded = true;
                    if (originAdded) {
                        changed.onLocationChanged(origin.getText(), destination.getText());
                    }
                } else {
                    destinationAdded = false;
                    changed.onLocationRemoved(origin.getText(), destination.getText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setOnItemClickListener(new LocationSuggestionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                origin.setText(suggestionsList.get(position).getOrigin());
                destination.setText(suggestionsList.get(position).getDestination());
            }
        });

        origin.getEditText().setFilters(new InputFilter[]{getFilter()});
        destination.getEditText().setFilters(new InputFilter[]{getFilter()});

        Route route = CurrentSession.getInstance().getCurrentRoute();
        if (((TransportEdit) getActivity()).isForEdit()) {
            origin.setText(route.getOrigin());
            destination.setText(route.getDestination());
        }

        return rootView;
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

    private InputFilter getFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) { // Accept only letter & digits ; otherwise just return
                        //
                        if (!String.valueOf(source.charAt(i)).equals("_")) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };
    }

    public void setAll(String origin, String destination) {

        this.origin.setText(origin);
        this.destination.setText(destination);
        originAdded = true;
        destinationAdded = true;

    }


    // Container Activity must implement this interface
    public interface OnStateChanged {

        void onLocationChanged(String origin, String destination);

        void onLocationRemoved(String origin, String destination);

    }

}
