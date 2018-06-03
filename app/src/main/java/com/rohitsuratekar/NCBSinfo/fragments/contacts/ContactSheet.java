package com.rohitsuratekar.NCBSinfo.fragments.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 19-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ContactSheet extends BottomSheetDialogFragment {

    private OnContactSheetSelected selected;
    private static String CONTACT = "contact";

    public static ContactSheet newInstance(ContactModel model) {
        ContactSheet myFragment = new ContactSheet();
        Bundle args = new Bundle();
        args.putString(CONTACT, new Gson().toJson(model));
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_sheet, container, false);

        TextView name = rootView.findViewById(R.id.ct_sheet_name);
        RecyclerView recyclerView = rootView.findViewById(R.id.ct_sheet_recycler);
        TextView subDetails = rootView.findViewById(R.id.ct_sheet_details);

        if (getActivity() instanceof BaseActivity && getArguments() != null) {
            final ContactModel model = new Gson().fromJson(getArguments().getString(CONTACT, ""), ContactModel.class);
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

            rootView.findViewById(R.id.ct_sheet_feedback).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@secretbiology.com", "ncbs.mod@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on " + model.getName() + " in contact list");
                    startActivity(Intent.createChooser(intent, "Send Email"));
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
            selected = (OnContactSheetSelected) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }

    public interface OnContactSheetSelected {
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
