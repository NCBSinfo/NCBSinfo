package com.rohitsuratekar.NCBSinfo.activities.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

/**
 * Created by Rohit Suratekar on 18-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class GeneralIntroFragment extends Fragment {

    private static final String IMAGE = "image";
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String TINTED = "tinted";

    public static GeneralIntroFragment newInstance(int image, int title, int message, boolean isTinted) {
        GeneralIntroFragment myFragment = new GeneralIntroFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE, image);
        args.putInt(TITLE, title);
        args.putInt(MESSAGE, message);
        args.putBoolean(TINTED, isTinted);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intro_general, container, false);
        ImageView imageView = rootView.findViewById(R.id.intro_general_icon);
        TextView message = rootView.findViewById(R.id.intro_general_message);
        TextView title = rootView.findViewById(R.id.intro_general_title);

        imageView.setImageResource(getArguments().getInt(IMAGE, R.drawable.intro1));
        title.setText(getString(getArguments().getInt(TITLE, R.string.intro_text1_heading)));
        message.setText(getString(getArguments().getInt(MESSAGE, R.string.intro_text1_details)));


        if (getArguments().getBoolean(TINTED, true)) {
            imageView.setColorFilter(General.getColor(getContext(), R.color.colorAccent));
        }
        return rootView;
    }
}
