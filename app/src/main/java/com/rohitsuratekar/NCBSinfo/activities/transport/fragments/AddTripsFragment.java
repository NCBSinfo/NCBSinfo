package com.rohitsuratekar.NCBSinfo.activities.transport.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TripEditAdapter;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripsFragment extends Fragment {

    @BindView(R.id.tp_fragment_trip_recycler)
    RecyclerView trips;
    @BindView(R.id.tp_fragment_add_trip_btn)
    Button addTripBtn;
    @BindView(R.id.tp_fragment_empty_icon)
    ImageView emptyIcon;
    @BindView(R.id.tp_fragment_trip_suggestions)
    TextView suggestions;
    @BindView(R.id.tp_fragment_spinner)
    Spinner spinner;
    private TripEditAdapter adapter;
    private List<String> items;
    private Calendar currentCalendar;
    sendDetails fragDetails;
    private boolean startBoolean = true; //To avoid first select of spinner

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_trips, container, false);
        ButterKnife.bind(this, rootView);
        currentCalendar = Calendar.getInstance();
        items = new ArrayList<>();
        adapter = new TripEditAdapter(items);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        //RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 1);
        trips.setLayoutManager(manager);
        trips.setAdapter(adapter);
        adapter.setOnItemClickListener(new TripEditAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.showOptions(position);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.deletItem(new TripEditAdapter.deletItem() {
            @Override
            public void onItemClick(int position) {
                items.remove(position);
                adapter.notifyDataSetChanged();
                fragDetails.setTripDetails(items, spinner.getSelectedItemPosition());
                if (items.size() == 0) {
                    emptyIcon.setVisibility(View.VISIBLE);
                    fragDetails.areTripsDone(false);
                }
                giveSuggestions();
            }
        });
        adapter.editItem(new TripEditAdapter.editItem() {
            @Override
            public void onItemClick(final int position) {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar = DateConverter.convertToCalender(ConverterMode.DATE_FIRST, items.get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                currentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                currentCalendar.set(Calendar.MINUTE, minute);
                                String temp = DateConverter.convertToString(currentCalendar, "hh:mm a").toUpperCase();
                                if (items.contains(temp)) {
                                    Toast.makeText(getContext(), "Already in the list", Toast.LENGTH_LONG).show();
                                } else {
                                    items.remove(position);
                                    items.add(temp);
                                    Collections.sort(items, new Comparator<String>() {
                                        @Override
                                        public int compare(String s1, String s2) {
                                            try {
                                                return DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s1)
                                                        .compareTo(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s2));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                return 0;
                                            }
                                        }
                                    });
                                    adapter.addItem(temp);
                                    adapter.notifyDataSetChanged();
                                    fragDetails.setTripDetails(items, spinner.getSelectedItemPosition());
                                }
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                dialog.show();

            }
        });

        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                currentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                currentCalendar.set(Calendar.MINUTE, minute);
                                String temp = DateConverter.convertToString(currentCalendar, "hh:mm a").toUpperCase();
                                if (items.contains(temp)) {
                                    Toast.makeText(getContext(), "Already in the list", Toast.LENGTH_LONG).show();
                                } else {
                                    emptyIcon.setVisibility(View.GONE);
                                    items.add(temp);
                                    Collections.sort(items, new Comparator<String>() {
                                        @Override
                                        public int compare(String s1, String s2) {
                                            try {
                                                return DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s1)
                                                        .compareTo(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s2));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                return 0;
                                            }
                                        }
                                    });
                                    adapter.addItem(temp);
                                    adapter.notifyDataSetChanged();
                                    fragDetails.setTripDetails(items, spinner.getSelectedItemPosition());
                                    fragDetails.areTripsDone(true);
                                    giveSuggestions();
                                }
                            }
                        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.transport_edit_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!startBoolean) {
                    fragDetails.setSpinner(position);
                } else {
                    startBoolean = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragDetails = (AddTripsFragment.sendDetails) context;
        } catch (ClassCastException e) {
            Log.e("Error", e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        for (String s : ((TransportEdit) getActivity()).getCurrentTrips()) {
            items.add(s);
            adapter.addItem(s);
        }
        adapter.notifyDataSetChanged();
        if (items.size() == 0) {
            emptyIcon.setVisibility(View.VISIBLE);
        } else {
            emptyIcon.setVisibility(View.GONE);
        }
        giveSuggestions();
        spinner.setSelection(((TransportEdit) getActivity()).getCurrentSpinnerItem());
    }

    public interface sendDetails {

        public List<String> setTripDetails(List<String> trips, int spinnerItem);

        public boolean areTripsDone(boolean isIt);

        public int setSpinner(int spn);

    }

    private void giveSuggestions() {

        switch (items.size()) {
            case 0:
                suggestions.setText(getString(R.string.sug_edit_transport_empty));
                break;
            case 1:
                suggestions.setText(getString(R.string.sug_edit_transport_first_element));
                break;
            default:
                suggestions.setText(getString(R.string.sug_edit_transport_second_element));
        }
    }
}
