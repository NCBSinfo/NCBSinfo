package com.rohitsuratekar.NCBSinfo.activities.transport.edit.trips;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportRecyclerItem;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTripsFragment extends Fragment {

    @BindView(R.id.add_trip_recycler)
    RecyclerView recyclerView;
    private OnStateChanged changed;

    @BindView(R.id.add_trip_title)
    TextView title;
    @BindView(R.id.add_trip_subtitle)
    TextView subtitle;
    @BindView(R.id.add_trip_note)
    TextView note;
    @BindView(R.id.add_trip_empty)
    TextView empty;

    private List<TransportRecyclerItem> list;
    private AddTripsAdapter adapter;
    private int selectedDay;
    private Calendar currentTime;
    private String[] allDays;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_edit_trips, container, false);
        ButterKnife.bind(this, rootView);
        list = new ArrayList<>();
        currentTime = Calendar.getInstance();
        adapter = new AddTripsAdapter(list);
        allDays = getContext().getResources().getStringArray(R.array.tripDays);
        title.setText(allDays[0]);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddTripsAdapter.onSelected() {
            @Override
            public void onItemSelected(int position) {
                if (!list.get(position).isSelected()) {
                    clearSelected();
                    list.get(position).setSelected(true);
                } else {
                    clearSelected();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemDeleted(int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
                changed.onTripChanged(convertToString(list), selectedDay);
            }
        });
        note.setText(getContext().getString(R.string.transport_set_trips_note1));
        if (list.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        Route route = CurrentSession.getInstance().getCurrentRoute();
        if (((TransportEdit) getActivity()).isForEdit()) {
            list.clear();
            List<String> passList = new ArrayList<>();
            for (String s : route.getDefaultList()) {
                try {
                    String dateString = DateConverter.changeFormat(ConverterMode.DATE_FIRST, s, "hh:mm a");
                    list.add(new TransportRecyclerItem(dateString, false));
                    passList.add(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            adapter.notifyDataSetChanged();
            selectedDay = ((TransportEdit) getActivity()).getDay();
            title.setText(allDays[selectedDay]);
            changed.onTripChanged(passList, selectedDay);
        }
        return rootView;
    }

    @OnClick({R.id.add_trip_title, R.id.add_trip_subtitle})
    public void showDaySelector() {
        clearSelected();
        adapter.notifyDataSetChanged();
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());

        b.setItems(allDays, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDay = which;
                title.setText(allDays[which]);
                dialog.dismiss();

            }
        });
        b.show();
    }

    @OnClick(R.id.add_trip_button)
    public void addTrip() {
        clearSelected();
        adapter.notifyDataSetChanged();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar tempCal = Calendar.getInstance();
                tempCal.set(Calendar.HOUR_OF_DAY, selectedHour);
                tempCal.set(Calendar.MINUTE, selectedMinute);
                String timeString = DateConverter.convertToString(tempCal, "hh:mm a");
                if (!containsItem(list, timeString)) {
                    list.add(new TransportRecyclerItem(timeString, false));
                    List<TransportRecyclerItem> tempList = new ArrayList<>(list);
                    Collections.sort(tempList, new Comparator<TransportRecyclerItem>() {

                        public int compare(TransportRecyclerItem o1, TransportRecyclerItem o2) {
                            try {
                                return DateConverter.convertToCalender(ConverterMode.DATE_FIRST, o1.getText())
                                        .compareTo(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, o2.getText()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    list.clear();
                    list.addAll(tempList);
                    adapter.notifyDataSetChanged();
                    currentTime.setTime(tempCal.getTime());
                    changed.onTripChanged(convertToString(list), selectedDay);
                    changeNote(list.size());
                    empty.setVisibility(View.GONE);
                } else {
                    General.makeShortToast(getContext(), "Already added!");
                }
            }
        }, hour, minute, false);
        mTimePicker.show();
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


    // Container Activity must implement this interface
    public interface OnStateChanged {
        void onTripChanged(List<String> currentTrips, int daySelected);
    }

    private boolean containsItem(List<TransportRecyclerItem> itemList, String s) {
        for (TransportRecyclerItem item : itemList) {
            if (item.getText().equals(s)) {
                return true;
            }
        }
        return false;
    }

    private List<String> convertToString(List<TransportRecyclerItem> itemList) {
        List<String> sL = new ArrayList<>();
        for (TransportRecyclerItem i : itemList) {
            sL.add(i.getText());
        }
        return sL;
    }

    private void clearSelected() {
        for (TransportRecyclerItem item : list) {
            item.setSelected(false);
        }
    }


    private void changeNote(int size) {
        switch (size) {
            case 0:
                note.setText(getContext().getString(R.string.transport_set_trips_note1));
                break;
            case 1:
                note.setText(getContext().getString(R.string.transport_set_trips_note2));
                break;
            case 2:
                note.setText(getContext().getString(R.string.transport_set_trips_note3));
                break;
            default:
                note.setText(getContext().getString(R.string.transport_set_trips_note4));
                break;
        }
    }

}
