package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 19-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ContactFragment extends BottomSheetDialogFragment {

    private OnContactSelected selected;
    private static String CONTACT = "contact";

    public static ContactFragment newInstance(ContactModel model) {
        ContactFragment myFragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(CONTACT, new Gson().toJson(model));
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_sheet, container, false);

        TextView name = rootView.findViewById(R.id.ct_sheet_name);
        RecyclerView recyclerView = rootView.findViewById(R.id.ct_sheet_recycler);
        TextView subDetails = rootView.findViewById(R.id.ct_sheet_details);

        if (getActivity() instanceof Contacts && getArguments() != null) {
            ContactModel model = new Gson().fromJson(getArguments().getString(CONTACT, ""), ContactModel.class);
            name.setText(model.getName());
            subDetails.setText(model.getInstitute());

            List<ContactDetailsModel> detailsModels = new ArrayList<>();
            detailsModels.add(new ContactDetailsModel("Extensions"));
            detailsModels.addAll(getExtensions(model));
            ContactDetailsModel loc = new ContactDetailsModel();
            loc.setName("Location");
            loc.setDetails(convertLocations(model.getLocation()));
            loc.setHasSubHeader(true);
            loc.setHeader(true);
            detailsModels.add(loc);

            ContactFragmentAdapter adapter = new ContactFragmentAdapter(detailsModels, new ContactFragmentAdapter.OnCallClick() {
                @Override
                public void onclick(String call) {
                    selected.onCalled(call);
                    dismiss();
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        }

        return rootView;
    }

    private List<ContactDetailsModel> getExtensions(ContactModel model) {
        List<ContactDetailsModel> models = new ArrayList<>();
        models.add(new ContactDetailsModel(model.getPrimaryExtension(), false));
        for (String s : model.getOtherExtensions()) {
            models.add(new ContactDetailsModel(s, false));
        }
        return models;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnContactSelected) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }

    public interface OnContactSelected {
        void onCalled(String call);
    }

    private String convertLocations(String loc) {
        return loc.replace("SLC", "Southern Lab Complex")
                .replace("ELC", "Eastern Lab Complex")
                .replace("NULL", "N/A")
                .replace("GF", ", Ground Floor")
                .replace("BS", ", Basement")
                .replace("FF", ", First Floor")
                .replace("SF", ", Second Floor")
                .replace("TF", ", Third Floor");
    }
}
